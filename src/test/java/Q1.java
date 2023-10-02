import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Q1 {

    static WebDriver driver;
    static File file;
    static FileInputStream fis;
    static XSSFWorkbook wb;
    static Sheet sh;

    static String firstName, lastName, postCode, fullName;

    public static void main(String[] args) throws IOException {

        System.out.println("SRIKANTH TEST ZONE");
        System.setProperty("webdriver.chrome.driver", "D:\\Tech\\workspace\\SrikanthProj\\utilities\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
        driver.manage().window().maximize();

        file = new File("D:\\Tech\\workspace\\SrikanthProj\\utilities\\EmpDetails.xlsx");
        fis = new FileInputStream(file);

        wb = new XSSFWorkbook(fis);
        sh = wb.getSheetAt(0);
        int lastRow = sh.getLastRowNum();


        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //Thread.sleep("10000");
        driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/div[2]/button")).click();

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


        ArrayList<String> listFirstName = new ArrayList<String>();
        ArrayList<String> listLastName = new ArrayList<String>();
        ArrayList<String> listPostCode = new ArrayList<String>();

        SoftAssert softAssert = new SoftAssert();

        for (int i = 1; i <= lastRow; i++) {

            //Click on Add Customer
            driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/button[1]")).click();

            firstName = sh.getRow(i).getCell(0).getStringCellValue();
            lastName = sh.getRow(i).getCell(1).getStringCellValue();
            postCode = sh.getRow(i).getCell(2).getStringCellValue();
            fullName = firstName + lastName;


            //First Name
            driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[1]/input")).sendKeys(firstName);
            //last name
            driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[2]/input")).sendKeys(lastName);
            //post code
            driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[3]/input")).sendKeys(postCode);
            //Click Add Customer
            driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/button")).click();

            Alert alert = driver.switchTo().alert();
            alert.accept();
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            //Customers
            driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[1]/button[3]")).click();
            driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/form/div/div/input")).sendKeys(firstName);
            String verifyElement = driver.findElement(By.xpath("/html/body/div/div/div[2]/div/div[2]/div/div/table/tbody/tr/td[1]")).getText();
            softAssert.assertTrue(verifyElement.equals(firstName), "True");
        }

        //softAssert.assertAll();


        //Web Table
        List<WebElement> rows = driver.findElements(By.xpath("//table[contains(@class, 'table')]//tbody//tr"));
        int rowCount = rows.size();
        System.out.println("Total No of rows: "+rowCount);

        String beforeXpath = "//table[contains(@class, 'table')]//tbody//tr[";
        String afterFNXpath = "]//td[1]";
        String afterLNXpath = "]//td[2]";
        String afterPCXpath = "]//td[3]";

        //First Name
        for(int j=1; j<=rowCount; j++) {
            String actualXpath = beforeXpath+j+afterFNXpath;
            WebElement ele = driver.findElement(By.xpath(actualXpath));
            System.out.println(ele.getText());
            listFirstName.add(ele.getText());
        }

        //Last Name
        for(int j=1; j<=rowCount; j++) {
            String actualXpath = beforeXpath+j+afterLNXpath;
            WebElement ele = driver.findElement(By.xpath(actualXpath));
            System.out.println(ele.getText());
            listLastName.add(ele.getText());
        }

        //Post Code
        for(int j=1; j<=rowCount; j++) {
            String actualXpath = beforeXpath+j+afterPCXpath;
            WebElement ele = driver.findElement(By.xpath(actualXpath));
            System.out.println(ele.getText());
            listPostCode.add(ele.getText());
        }


    }
}

