package com.username.dataproviders;

import com.username.config.ConfigReader;
import com.username.models.Customer;
import com.username.utils.CsvUtils;
import com.username.utils.ExcelUtils;
import com.username.utils.JsonUtils;
import org.testng.annotations.DataProvider;

import java.util.List;
import java.util.Map;

/**
 * All data providers in one class. A test asks for one by name:
 * {@code @Test(dataProvider = "loginData", dataProviderClass = TestDataProviders.class)}.
 * Each row of the file becomes one run of the test.
 */
public final class TestDataProviders {

    /** Excel: testdata/LoginData.xlsx, sheet "Login". */
    @DataProvider(name = "loginData")
    public static Object[][] loginData() {
        List<Map<String, String>> rows = ExcelUtils.readSheet(ConfigReader.get("testdata.excel"), "Login");
        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> r = rows.get(i);
            data[i] = new Object[]{r.get("username"), r.get("password"), r.get("expectedResult"), r.get("expectedMessage")};
        }
        return data;
    }

    /** CSV: testdata/products.csv */
    @DataProvider(name = "productData")
    public static Object[][] productData() {
        List<Map<String, String>> rows = CsvUtils.read(ConfigReader.get("testdata.csv"));
        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = new Object[]{rows.get(i).get("product"), Double.parseDouble(rows.get(i).get("price"))};
        }
        return data;
    }

    /** JSON: testdata/customers.json, read into Customer objects. */
    @DataProvider(name = "customerData")
    public static Object[][] customerData() {
        List<Customer> customers = JsonUtils.readList(ConfigReader.get("testdata.json"), Customer.class);
        Object[][] data = new Object[customers.size()][];
        for (int i = 0; i < customers.size(); i++) {
            data[i] = new Object[]{customers.get(i)};
        }
        return data;
    }
}
