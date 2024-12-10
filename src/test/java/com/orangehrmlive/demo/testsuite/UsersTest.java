package com.orangehrmlive.demo.testsuite;

import com.orangehrmlive.demo.customlisteners.CustomListeners;
import com.orangehrmlive.demo.pages.*;
import com.orangehrmlive.demo.testbase.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import resources.testdata.TestData;

@Listeners(CustomListeners.class)
public class UsersTest extends BaseTest {

    HomePage homePage;
    LoginPage loginPage;
    SideMenuPage sideMenuPage;
    AdminPage adminPage;
    ViewSystemUserPage viewSystemUserPage;
    AddUserPage addUserPage;

    @BeforeMethod(alwaysRun = true)
    public void inIt() {
        homePage = new HomePage();
        loginPage = new LoginPage();
        sideMenuPage = new SideMenuPage();
        adminPage = new AdminPage();
        viewSystemUserPage = new ViewSystemUserPage();
        addUserPage = new AddUserPage();
    }

    @Test(groups = {"sanity"})
    public void adminShouldAddUserSuccessFully() throws InterruptedException {

        loginPage.enterUserNameAndPasswordForLogin("Admin", "admin123");

        sideMenuPage.clickOnTab("Admin");
        Assert.assertEquals(adminPage.getPageTitle(), "System Users");
        adminPage.clickOnAddButton();
        Assert.assertEquals(addUserPage.getAdduserPageTitle(), "Add User");
        addUserPage.selectUserRoleDropdown("Admin");
        Thread.sleep(2000);
        addUserPage.enterEmployeeName("a");
        Thread.sleep(2000);
        addUserPage.enterUserName("prime test");
        addUserPage.selectStatusDropdown("Disable");
        addUserPage.enterPassword("Test123456");
        Thread.sleep(2000);
        addUserPage.enterConfirmPassword("Test123456");
        addUserPage.clickOnSaveButton();
        Thread.sleep(2000);
        Assert.assertEquals(addUserPage.getSuccessMessage(), "Successfully Saved");

    }

    @Test(groups = {"sanity", "smoke"})
    public void searchTheUserCreatedAndVerifyIt() {

        String username = "prime test";

        loginPage.enterUserNameAndPasswordForLogin("Admin", "admin123");

        sideMenuPage.clickOnTab("Admin");
        Assert.assertEquals(adminPage.getPageTitle(), "System Users");

        adminPage.enterUserName(username);
        adminPage.selectUserRoleFromDropdown("Admin");
        adminPage.selectStatusFromDropdown("Disable");
        adminPage.clickOnSearchButton();
        Assert.assertEquals(adminPage.getUsernameFromSearchResult(), username);

    }

    @Test(groups = {"regression"})
    public void verifyThatAdminShouldDeleteTheUserSuccessFully() {

        String username = "prime test";

        loginPage.enterUserNameAndPasswordForLogin("Admin", "admin123");

        sideMenuPage.clickOnTab("Admin");
        Assert.assertEquals(adminPage.getPageTitle(), "System Users");

        adminPage.enterUserName(username);
        adminPage.selectUserRoleFromDropdown("Admin");
        adminPage.selectStatusFromDropdown("Disable");
        adminPage.clickOnSearchButton();

        Assert.assertEquals(adminPage.getUsernameFromSearchResult(), username);

        adminPage.selectTheCheckboxOfUserNameResult();
        adminPage.clickOnDeleteButtonInList();
        adminPage.clickOnYesDeleteButtonOnPopUp();
        Assert.assertEquals(viewSystemUserPage.getSuccessDeleteMessage(), "Successfully Deleted");

    }


    @Test(groups = {"regression"}, dataProvider = "usertests", dataProviderClass = TestData.class)
    public void searchTheUserAndVerifyTheMessageRecordFound
            (String username, String userRole, String employeeName, String status) throws InterruptedException {
        loginPage.enterUserNameAndPasswordForLogin("Admin", "admin123");
        sideMenuPage.clickOnTab("Admin");
        Assert.assertEquals(adminPage.getPageTitle(), "System Users");
        adminPage.enterUserName(username);
        adminPage.selectUserRoleFromDropdown(userRole);
        adminPage.enterEmployeeName(employeeName);
        adminPage.selectStatusFromDropdown(status);
        adminPage.clickOnSearchButton();
        Assert.assertEquals(adminPage.getRecordsFound(), "(1) Record Found");
        Assert.assertEquals(adminPage.getUsernameFromSearchResult(), username);
        adminPage.clickOnResetButton();
    }

}
