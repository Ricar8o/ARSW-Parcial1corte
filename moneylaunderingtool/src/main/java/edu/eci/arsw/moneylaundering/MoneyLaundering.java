package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering {
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;

    public MoneyLaundering() {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
    }

    public void processTransactionData() {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        for (File transactionFile : transactionFiles) {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for (Transaction transaction : transactions) {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
    }

    public void processTransactionData(int ini, int fin) {
        List<File> transactionFiles = getTransactionFileList();
        for (int i = ini; i < fin; i++) {
            synchronized(amountOfFilesProcessed){
                System.out.println(amountOfFilesTotal + " " + fin);
                File transactionFile = transactionFiles.get(i);
                List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
                for (Transaction transaction : transactions) {
                    transactionAnalyzer.addTransaction(transaction);
                }
                amountOfFilesProcessed.incrementAndGet();
            }
        }
    }

    public void define() {
        amountOfFilesProcessed.set(0);
        amountOfFilesTotal = getTransactionFileList().size();
    }

    public List<String> getOffendingAccounts() {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList() {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/"))
                .filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args) {
        System.out.println(getBanner());
        System.out.println(getHelp());
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        int tamaño = moneyLaundering.getTransactionFileList().size();
        moneyLaundering.define();
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData(0, tamaño / 5));
        Thread processingThread2 = new Thread(() -> moneyLaundering.processTransactionData(tamaño / 5, (tamaño*2)/5));
        Thread processingThread3 = new Thread(() -> moneyLaundering.processTransactionData((tamaño*2)/5, (tamaño*3)/5));
        Thread processingThread4 = new Thread(() ->  moneyLaundering.processTransactionData((tamaño*3)/5, (tamaño*4)/5));
        Thread processingThread5 = new Thread(() -> moneyLaundering.processTransactionData((tamaño*4)/5, tamaño));
    
        processingThread.start();
        processingThread2.start();
        processingThread3.start();
        processingThread4.start();
        processingThread5.start();
        while(true)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
            {
                System.exit(0);
            }

            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
            line = scanner.nextLine();
        }
    }

    private static String getBanner()
    {
        String banner = "\n";
        try {
            banner = String.join("\n", Files.readAllLines(Paths.get("src/main/resources/banner.ascii")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return banner;
    }

    private static String getHelp()
    {
        String help = "Type 'exit' to exit the program. Press 'Enter' to get a status update\n";
        return help;
    }
}