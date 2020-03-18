package com.company;

import java.io.*;
import java.math.RoundingMode;
import java.util.*;


import org.apache.commons.math3.util.Precision;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.nio.cs.ext.MacArabic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.pow;

public class Main {

    public static void main(String[] args) {

        XSSFWorkbook workbook= new XSSFWorkbook();

        XSSFSheet sheet= workbook.createSheet("laba1");

        int rowNum=0;
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue("Эмпирические частоты Nm\n");
        row.createCell(1).setCellValue("Вероятности для эмпирических частот P(Nm)\n");
        row.createCell(2).setCellValue("Теоретические вероятности Pm\n");


        try {
            FileWriter writer = new FileWriter("/Users/maksim/projectsMaven/laba1/resource/teorver.txt",false);
            Double sum = 0.0;
            ArrayList<Double> Fm = new ArrayList<Double>();
            ArrayList<Integer> xi = new ArrayList<Integer>();
            Double[] gamma = new Double[100];
            FileReader fr= new FileReader("/Users/maksim/projectsMaven/laba1/resource/gamma.txt");
            BufferedReader reader=new BufferedReader(fr);
            int countLine=1;

            Double Pm = Precision.round(0.25 * pow(0.75, 0),3);
            writer.write(String.valueOf(Pm+"\n"));
            Fm.add(0.25 * pow(0.75, 0));
            for (int i = 2; i <= 22; i++) {
                Pm =Precision.round(0.25 * pow(0.75, i - 1),3);
                writer.write(String.valueOf(Pm)+"\n");
                Fm.add(0.25 * pow(0.75, i - 1) + Fm.get(i - 2));
                countLine++;

            }
            writer.flush();

            for (int i = 0; i < gamma.length; i++) {
                gamma[i] = Precision.round(Double.valueOf(reader.readLine()),3);
            }

            writer = new FileWriter("/Users/maksim/projectsMaven/laba1/resource/xi.txt",false);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < Fm.size(); j++) {
                    if (gamma[i] <= Fm.get(j)) {
                       xi.add(j);
                        break;
                    }
                }
            }
            for (Integer e:xi) {
                writer.write(e+"\n");
            }
                writer.flush();
            Map<Integer, Integer> viborka = new HashMap<>();
            for (int i = 0; i < xi.size(); i++) {
                if (viborka.containsKey(xi.get(i))) {
                    viborka.replace(xi.get(i), viborka.get(xi.get(i)), viborka.get(xi.get(i)) + 1);
                } else {
                    viborka.put(xi.get(i), 1);
                }
            }
            writer= new FileWriter("/Users/maksim/projectsMaven/laba1/resource/viborka.txt",false);


            Map<Integer, Double> impech = new HashMap<>();
            for (int i = 0; i <viborka.size(); i++) {
                for (Map.Entry<Integer, Integer> nV : viborka.entrySet()) {
                    if (nV.getValue().equals(viborka.get(i))){
                        impech.put(nV.getKey(), viborka.get(i) / 100.0);

                    }
                }
            }

            for (Map.Entry<Integer, Integer> nV : viborka.entrySet()) {
                writer.write(nV.getKey()+" = "+nV.getValue()+"\n");
            }
            writer.flush();
            writer= new FileWriter("/Users/maksim/projectsMaven/laba1/resource/impech.txt",false);
            int count=1;
            fr= new FileReader("/Users/maksim/projectsMaven/laba1/resource/teorver.txt");
            reader=new BufferedReader(fr);
            for (Map.Entry<Integer, Double> nV : impech.entrySet()) {
                row = sheet.createRow(count);
                writer.write(nV.getKey()+" = "+nV.getValue()+"\n");
                String s=String.valueOf(reader.readLine());
                if(s!= "null") {
                    //String n= s.replaceAll("\\.",",");
                    row.createCell(2).setCellValue(Double.valueOf(s));
                }
                row.createCell(0).setCellValue(nV.getKey());
                row.createCell(1).setCellValue(nV.getValue());
                count++;
            }

            writer.flush();
           if(count<countLine) {
               for (int i = count; i < countLine; i++) {

                   row = sheet.createRow(i);

                   String s = String.valueOf(reader.readLine());
                   if (s != "null") {
                       row.createCell(2).setCellValue(Double.valueOf(s));
                   }
               }
           }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);


            FileOutputStream out = new FileOutputStream( new File("file.xlsx"));
            workbook.write(out);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    }

