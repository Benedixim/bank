/**
 * Класс, представляющий счет пользователя.
 * Содержит информацию счете: названии, балансе, дате открытия и другое.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.database.DatabaseHandler;
import org.example.strategy.Transaction;

import java.io.*;
import java.util.List;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    /**
     * идентификатор счета.
     */
    private int id;

    /**
     * Название счета.
     */
    private String name;

    /**
     * Валюта счета.
     */
    private String currency;

    /**
     * Баланс счета.
     */
    private double balance;

    /**
     * Дата открытия счета.
     */
    private Date openingDate;

    /**
     * Дата последнего начисления процентов на счет.
     */
    private Date  lastInterestDate;

    /**
     * идентификатор банка, в котором был открыт счёт.
     */
    private int bank_id;

    /**
     * идентификатор пользователя, которому принадлежит счёт.
     */
    private int user_id;

    /**
     * Метод print выводит на консоль выписку в виде текста.
     * @param period период, за который необходимо сформировать выписку (например, "month")
     */
    public void print(String period)
    {
        System.out.println(generateStatement(period));
    }

    /**
     Метод saveToFile предоставляет выбор формата файла (PDF либо TXT) для сохранения выписки,
     с последующим вызовом соответствующей функции записи.
     @param period период, за который необходимо сформировать выписку (например, "month")
     */
    public void saveToFile(String period){
        Scanner in = new Scanner(System.in);
        List<String> formats = new ArrayList<>();
        formats.add("PDF");
        formats.add("TXT");

        String format;

        while(true) {
            System.out.println("Выберите формат: " + formats);
            System.out.print("--> ");
            format = in.nextLine();
            if (!formats.contains(format))
                System.out.println("Введенный вами формат не подходит! Попробуйте ещё раз!");
            else break;
        }

        switch (format) {
            case "PDF" -> this.statementToPdf(period);
            case "TXT" -> this.statementToTxt(period);
            default -> {
            }
        }
    }

    /**
    Метод statementToTxt создает файл для выписки и записывает в него информацию о выписке.
    Файл сохраняется в директории "statement" с названием вида "statement <ГОД>-<МЕСЯЦ>-<ДЕНЬ> <ЧАС>-<МиНУТА>.txt".
    Если файл не существует, он будет создан.
    @param period период, за который необходимо сформировать выписку (например, "month")
    */
    public void statementToTxt(String period) {
        try {
            Calendar calendar = Calendar.getInstance();
            // создаем файл для чека
            File file = new File("statement/statement" + this.getId() + " " + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + ".txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // открываем поток для записи в файл
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(generateStatement(period)); // записываем информацию в файл

            // закрываем потоки
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Метод statementToPDF создает файл для выписки и записывает в него информацию о выписке.
     Файл сохраняется в директории "statement" с названием вида "statement <ГОД>-<МЕСЯЦ>-<ДЕНЬ> <ЧАС>-<МиНУТА>.pdf".
     Если файл не существует, он будет создан.
     @param period период, за который необходимо сформировать выписку (например, "month")
     */
    public void statementToPdf(String period){

        Calendar calendar = Calendar.getInstance();
        Document document = new Document();

        Font font = null;
        try {

            BaseFont baseFont = BaseFont.createFont("Courier New Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            font = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            PdfWriter.getInstance(document, new FileOutputStream("statement/statement" + this.getId() + " " + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + ".pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        document.open();
        Paragraph paragraph = new Paragraph(generateStatement(period), font);

        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        document.close();
    }


    /**
     Метод generateStatement создает строку, содержащую всю информацию в выписке в виде текста для последующей записи в файл.
     @param period период, за который необходимо сформировать выписку (например, "month")
     */
    public String generateStatement(String period) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Calendar calendar = Calendar.getInstance();

        Calendar calendar2 = (Calendar) calendar.clone();

        switch (period) {
            case "month" -> calendar2.add(Calendar.MONTH, -1);
            case "year" -> calendar2.add(Calendar.YEAR, -1);
            case "all" -> calendar2.setTime(this.getOpeningDate());
            default -> {
            }
        }

        StringBuilder statement = new StringBuilder();
        statement.append("-".repeat(60)).append("\n");
        statement.append(" ".repeat(27)).append("Выписка").append("\n");
        statement.append(" ".repeat(27)).append(dbHandler.getBank(this.getBank_id()).getName()).append("\n");
        statement.append(String.format("%-32s | %s\n", "Клиент", dbHandler.getUser(this.getUser_id()).getName()));
        statement.append(String.format("%-30s | %s\n", "Счет", this.getName()));
        statement.append(String.format("%-32s | %s\n", "Валюта", this.getCurrency()));
        statement.append(String.format("%-32s | %s - %s\n",  "Период", calendar2.get(Calendar.DAY_OF_MONTH) + "." + (calendar2.get(Calendar.MONTH)+1) + "." + calendar2.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.YEAR)));//////////текущая дата и время, снова через календарь
        statement.append(String.format("%-48s | %s\n",  "Дата и время формирования", calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.YEAR)+ ", " +  calendar.get(Calendar.HOUR_OF_DAY) + "." +  calendar.get(Calendar.MINUTE)));
        statement.append(String.format("%-33s | %.2f %s\n", "Остаток", this.getBalance(), this.getCurrency()));
        statement.append("-".repeat(60)).append("\n");
        statement.append("   Дата    |").append(" ".repeat(12)).append("Примечание").append(" ".repeat(12)).append("| Сумма       ").append("\n");
        statement.append("-".repeat(60)).append("\n");

        List<Transaction> transactions = dbHandler.getTransactions(this.getId(), calendar2);
        for (Transaction tr : transactions) {
            statement.append(String.format("%10s |", tr.getCalendar().get(Calendar.DAY_OF_MONTH) + "-" + (tr.getCalendar().get(Calendar.MONTH) + 1) + "-" + tr.getCalendar().get(Calendar.YEAR)));
            statement.append(String.format(" %-8s", tr.getType()));
            if (tr.getType().equals("transfer"))
                if (tr.getAccount1().equals(this))
                {statement.append(String.format(" to %-20s", dbHandler.getUser(tr.getAccount2().getUser_id()).getName())); tr.setAmount(tr.getAmount() * -1);}
                else
                    statement.append(String.format(" from %-18s", dbHandler.getUser(tr.getAccount1().getUser_id()).getName()));
            else
                statement.append(" ".repeat(24));
            if(tr.getType().equals("withdraw")) statement.append(String.format(" | %.2f %s\n", (-1* tr.getAmount()), tr.getCurrency()));
            else statement.append(String.format(" | %.2f %s\n", tr.getAmount(), tr.getCurrency()));
        }
        statement.append("-".repeat(60)).append("\n");
        return statement.toString();
    }
}
