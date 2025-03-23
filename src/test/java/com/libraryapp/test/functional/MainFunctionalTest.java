package com.libraryapp.test.functional;

import static com.libraryapp.test.utils.TestUtils.businessTestFile;
import static com.libraryapp.test.utils.TestUtils.currentTest;
import static com.libraryapp.test.utils.TestUtils.testReport;
import static com.libraryapp.test.utils.TestUtils.yakshaAssert;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import mainapp.MyApp;

public class MainFunctionalTest {

    @AfterAll
    public static void afterAll() {
        testReport();
    }

    @Test
    @Order(1)
    public void testGitInitialized() throws IOException {
        try {
            // Calling the method to check if Git is initialized
            String output = MyApp.isGitInitialized();
            System.out.println(output);
            yakshaAssert(currentTest(), output.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(2)
    public void testCommitMessageExists() throws IOException {
        try {
            // Calling the method to check if the commit with message 'add untracked.txt' exists
            String output = MyApp.isCommitPresent();
            // System.out.println(output);
            yakshaAssert(currentTest(), output.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(3)
    public void testUntrackedFileRemoved() throws IOException {
        try {
            // Calling the method to check if the commit with the message 'add untracked.txt' contains the file
            String output = MyApp.wasUntrackedFileRemovedOrNot();  // Reusing the same method
            if (output.equals("true")) {
                // Assuming `isCommitPresent()` prints the commit details where "untracked.txt" is found
                yakshaAssert(currentTest(), true, businessTestFile);
            } else {
                yakshaAssert(currentTest(), false, businessTestFile);
            }
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }
}