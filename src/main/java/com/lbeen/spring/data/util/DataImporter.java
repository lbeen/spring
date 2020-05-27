package com.lbeen.spring.data.util;

import com.lbeen.spring.common.runable.AssemblyLine;
import com.lbeen.spring.common.runable.AssemblyLineWorkerFactory;
import com.lbeen.spring.common.runable.Consumer;
import com.lbeen.spring.common.runable.Producer;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.MongoUtil;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

public class DataImporter {
    private final File file;
    private final String split;
    private final String tableName;
    private final List<BiConsumer<Document, String[]>> valuePuts;

    private long total = 0;
    private final AtomicLong imported = new AtomicLong(0);

    private final AssemblyLine<Document> assemblyLine;

    public DataImporter(File file, String split, String tableName, List<BiConsumer<Document, String[]>> valuePuts) {
        this.file = file;
        this.split = split;
        this.tableName = tableName;
        this.valuePuts = valuePuts;
        this.assemblyLine = createAssemblyLine();
    }

    public void importData() {
        assemblyLine.run();
    }

    public long getTotal() {
        return total;
    }

    public long getImported() {
        return imported.get();
    }

    public boolean isFinish() {
        return assemblyLine.isFinish();
    }

    private AssemblyLine<Document> createAssemblyLine() {
        AssemblyLineWorkerFactory<Document> workerFactory = new AssemblyLineWorkerFactory<Document>() {
            @Override
            public Producer<Document> producer() {
                return queue -> {
                    try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
                        reader.skip(Long.MAX_VALUE);
                        total = reader.getLineNumber();
                    }
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] data = line.split(split);

                            Document document = new Document();
                            for (BiConsumer<Document, String[]> valuePut : valuePuts) {
                                valuePut.accept(document, data);
                            }
                            if (document.isEmpty()) {
                                continue;
                            }
                            document.put("_id", CommonUtil.uuid());

                            queue.offer(document);
                        }
                    }
                };
            }

            @Override
            public Consumer<Document> consumer() {
                return new Consumer<Document>() {
                    private final List<Document> inserts = new ArrayList<>();

                    @Override
                    public void consume(Document document) {
                        inserts.add(document);
                        if (inserts.size() == 5000) {
                            MongoUtil.insertList(tableName, inserts);
                            imported.addAndGet(5000);
                            inserts.clear();
                        }
                    }

                    @Override
                    public void finish() {
                        if (!inserts.isEmpty()) {
                            MongoUtil.insertList(tableName, inserts);
                            imported.addAndGet(inserts.size());
                        }
                    }
                };
            }
        };

        return AssemblyLine.singleProducer(workerFactory, 2);
    }
}
