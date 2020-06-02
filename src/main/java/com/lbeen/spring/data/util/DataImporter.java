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
import java.util.concurrent.LinkedBlockingQueue;
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
                        String line = br.readLine();
                        if (line == null) {
                            return;
                        }
                        addToQueue(removeUtf8Prefix(line), queue);

                        while ((line = br.readLine()) != null) {
                            addToQueue(line, queue);
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
                            insertData(inserts);
                        }
                    }

                    @Override
                    public void finish() {
                        if (!inserts.isEmpty()) {
                            insertData(inserts);
                        }
                    }
                };
            }
        };

        return AssemblyLine.singleProducer(workerFactory, 5);
    }

    private String removeUtf8Prefix(String line) {
        byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 3 || bytes[0] != -17 || bytes[1] != -69 || bytes[2] != -65) {
            return line;
        }
        byte[] dest = new byte[bytes.length - 3];
        System.arraycopy(bytes, 3, dest, 0, dest.length);
        return new String(dest, StandardCharsets.UTF_8);
    }

    private void addToQueue(String line, LinkedBlockingQueue<Document> queue) throws Exception {
        String[] data = line.split(split);

        Document document = new Document();
        for (BiConsumer<Document, String[]> valuePut : valuePuts) {
            valuePut.accept(document, data);
        }
        if (document.isEmpty()) {
            return;
        }
        document.put("_id", CommonUtil.uuid());

        queue.put(document);
    }

    private void insertData(List<Document> inserts) {
        MongoUtil.insertList(tableName, inserts);
        imported.addAndGet(inserts.size());
        inserts.clear();
    }
}
