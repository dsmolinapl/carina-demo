package com.zebrunner.carina.demo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.core.registrar.tag.Priority;
import com.zebrunner.carina.core.registrar.tag.TestPriority;
import com.zebrunner.carina.demo.gui.components.ModelItem;
import com.zebrunner.carina.demo.gui.components.NewsItem;
import com.zebrunner.carina.demo.gui.pages.common.AllBrandsPageBase;
import com.zebrunner.carina.demo.gui.pages.common.BrandModelsPageBase;
import com.zebrunner.carina.demo.gui.pages.common.HomePageBase;
import com.zebrunner.carina.demo.gui.pages.common.NewsPageBase;

public class WebPassSimpleTest implements IAbstractTest {


    @Test
    @MethodOwner(owner = "qpsdemo")
    @TestLabel(name = "feature", value = {"web", "acceptance"})
    public void testNewsSearch() {
        HomePageBase homePage = initPage(getDriver(), HomePageBase.class);
        homePage.open();
        Assert.assertTrue(homePage.isPageOpened(), "Home page is not opened!");

        NewsPageBase newsPage = homePage.getFooterMenu().openNewsPage();
        Assert.assertTrue(newsPage.isPageOpened(), "News page is not opened!");

        final String searchQ = "iphone";
        List<NewsItem> news = newsPage.searchNews(searchQ);
        Assert.assertFalse(CollectionUtils.isEmpty(news), "News not found!");
        SoftAssert softAssert = new SoftAssert();
        for (NewsItem n : news) {
            System.out.println(n.readTitle());
            softAssert.assertTrue(StringUtils.containsIgnoreCase(n.readTitle(), searchQ),
                    "Invalid search results for " + n.readTitle());
        }
        softAssert.assertAll();
    }

    @Test()
    @MethodOwner(owner = "qpsdemo")
    @TestPriority(Priority.P3)
    @TestLabel(name = "feature", value = {"web", "regression"})
    public void testBrandGroup() {
        HomePageBase homePage = initPage(getDriver(), HomePageBase.class);
        homePage.open();
        Assert.assertTrue(homePage.isPageOpened(), "Home page is not opened!");

        AllBrandsPageBase allBrandsPage = homePage.openAllBrandsPage();
        Assert.assertTrue(allBrandsPage.isPageOpened(), "All mobile phone brands page is not opened!");

        final String brandName = "Lava";
        BrandModelsPageBase brandModelsPage = allBrandsPage.selectBrand(brandName);
        List<ModelItem> models = brandModelsPage.getModels();
        SoftAssert softAssert = new SoftAssert();
        for (ModelItem modelItem : models) {
            softAssert.assertFalse(modelItem.readModel().contains(brandName),
                    "Model " + modelItem.readModel() + " should not include brand " + brandName + " in title");
        }

        softAssert.assertAll();
    }

}
