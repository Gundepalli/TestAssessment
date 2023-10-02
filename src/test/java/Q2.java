import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Q2 {

    static WebDriver driver;
    static File file;
    static FileInputStream fis;
    static XSSFWorkbook wb;
    static Sheet sh;

    public static void main(String[] args) throws IOException {

        System.setProperty("webdriver.chrome.driver", "D:\\Tech\\workspace\\SrikanthProj\\utilities\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        //Excel
        file = new File("D:\\Tech\\workspace\\SrikanthProj\\utilities\\EmpDetails.xlsx");
        fis = new FileInputStream(file);
        wb = new XSSFWorkbook(fis);
        sh = wb.getSheet("Transactions");
        int lastRow = sh.getLastRowNum();
        ArrayList<String> actualAmount = new ArrayList<>();
        ArrayList<String> actualBalance = new ArrayList<>();
        for (int i = 1; i <= lastRow; i++) {

            //Amount
            double doubleAmount = sh.getRow(i).getCell(0).getNumericCellValue();
            long amount = (long)doubleAmount;
            actualAmount.add(String.valueOf(amount));
            System.out.println("Excel Amount: "+amount);

            //Current Balance
            double doubleVar = sh.getRow(i).getCell(2).getNumericCellValue();
            long currentBalance = (long)doubleVar;
            actualBalance.add(String.valueOf(currentBalance));
            System.out.println("Excel Balance: "+actualBalance);
        }



        //Customer Login
        driver.findElement(By.xpath("//button[contains(text(), 'Customer Login')]")).click();
        Select select = new Select(driver.findElement(By.xpath("//*[@id='userSelect']")));
        select.selectByVisibleText("Hermoine Granger");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        select = new Select(driver.findElement(By.xpath("//*[@id='accountSelect']")));
        select.selectByVisibleText("1003");

        //Initial Balance
        int initialBalance = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText());
        System.out.println("initialBalance: "+initialBalance);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(initialBalance, 0);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        //Credit
        driver.findElement(By.xpath("//button[@ng-click='deposit()']")).click();
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys(actualAmount.get(0));
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        long balAmount = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText());
        long amt = Long.parseLong(actualAmount.get(1).trim());
        long curBal = Long.parseLong(actualBalance.get(0).trim());
        long totalBalance = balAmount+initialBalance;
        softAssert.assertEquals(curBal, totalBalance);
        System.out.println("Transaction 1: "+actualBalance.get(0)+", Total Balance: "+totalBalance);

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='error ng-binding']")));
        String errorMsg = driver.findElement(By.xpath("//span[@class='error ng-binding']")).getText();
        softAssert.assertTrue("Deposit Successful".equals(errorMsg), "Amount Deposit Successfully");

        //Debit - 3000
        driver.findElement(By.xpath("//button[@ng-click='withdrawl()']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Amount to be Withdrawn')]")));
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys(actualAmount.get(1));
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        balAmount = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText().trim());
        amt = Long.parseLong(actualAmount.get(1).trim());
        curBal = Long.parseLong(actualBalance.get(1).trim());
        totalBalance = totalBalance-amt;
        softAssert.assertEquals(curBal, totalBalance);
        System.out.println("Transaction 2: "+actualBalance.get(1)+", Total Balance: "+totalBalance);

        //Debit - 2000
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys(actualAmount.get(2));
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        balAmount = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText().trim());
        amt = Long.parseLong(actualAmount.get(2).trim());
        curBal = Long.parseLong(actualBalance.get(2).trim());
        totalBalance = totalBalance-amt;
        softAssert.assertEquals(curBal, totalBalance);
        System.out.println("Transaction 3: "+actualBalance.get(2)+", Total Balance: "+totalBalance);

        //Credit - 5000
        driver.findElement(By.xpath("//button[@ng-click='deposit()']")).click();
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys(actualAmount.get(3));
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        balAmount = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText());
        amt = Long.parseLong(actualAmount.get(3).trim());
        curBal = Long.parseLong(actualBalance.get(3).trim());
        totalBalance = totalBalance+amt;
        softAssert.assertEquals(curBal, totalBalance);
        System.out.println("Transaction 4: "+actualBalance.get(3)+", Total Balance: "+totalBalance);

        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='error ng-binding']")));
        errorMsg = driver.findElement(By.xpath("//span[@class='error ng-binding']")).getText();
        softAssert.assertTrue("Deposit Successful".equals(errorMsg), "Amount Deposit Successfully");

        //Debit - 10000
        driver.findElement(By.xpath("//button[@ng-click='withdrawl()']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Amount to be Withdrawn')]")));
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys(actualAmount.get(4));
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        balAmount = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText().trim());
        amt = Long.parseLong(actualAmount.get(4).trim());
        curBal = Long.parseLong(actualBalance.get(4).trim());
        totalBalance = totalBalance-amt;
        softAssert.assertEquals(curBal, totalBalance);
        System.out.println("Transaction 5: "+actualBalance.get(4)+", Total Balance: "+totalBalance);


        //Debit - 15000
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys(actualAmount.get(5));
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        balAmount = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText().trim());
        amt = Long.parseLong(actualAmount.get(5).trim());
        curBal = Long.parseLong(actualBalance.get(5).trim());
        totalBalance = totalBalance-amt;
        softAssert.assertEquals(curBal, totalBalance);
        System.out.println("Transaction 6: "+actualBalance.get(5)+", Total Balance: "+totalBalance);

        //Credit - 1500
        driver.findElement(By.xpath("//button[@ng-click='deposit()']")).click();
        driver.findElement(By.xpath("//input[@type='number']")).sendKeys(actualAmount.get(6));
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='error ng-binding']")));
        errorMsg = driver.findElement(By.xpath("//span[@class='error ng-binding']")).getText();
        softAssert.assertTrue("Deposit Successful".equals(errorMsg), "Amount Deposit Successfully");

        balAmount = Integer.parseInt(driver.findElement(By.xpath("//div[@class='center']//strong[2]")).getText());
        amt = Long.parseLong(actualAmount.get(6).trim());
        curBal = Long.parseLong(actualBalance.get(6).trim());
        totalBalance = totalBalance+amt;
        softAssert.assertEquals(curBal, totalBalance);
        System.out.println("Transaction 7: "+actualBalance.get(6)+", Total Balance: "+totalBalance);

        softAssert.assertAll();

    }
}
