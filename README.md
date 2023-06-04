# GuessNumberActivity
Guess Number Homework

<a href='https://play.google.com/store/apps/details?id=com.mason.myapplication&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width='300' alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

GuessNumber 是一個由作者在閒暇時間開發的小程式，主要用於實作新學習的技術。  
以下是專案的主要特點：


* 架構: GuessNumber 使用 MVVM 架構，以確保程式碼的組織和可測試性。搭配 Navigation，便於頁面轉換設計。

* 會員機制: 串接 Firebase Authentication 實現會員機制，使用者可以註冊和登錄帳號。

* 會員資料存儲: 串接 Firebase Realtime Database，使用者可以登錄後更改暱稱和圖像。

* 猜對次數資料儲存: 遊戲結束後，使用者可以輸入暱稱並將猜對的次數保存到資料庫，使用 Room 處理資料庫操作。

* 猜數字遊戲: 使用者可以進入猜數字遊戲，遊戲頁面提供猜數字按鈕。   
使用ViewModel搭配LiveData實作，並使用DataBinding即時更新UI介面。

* 單元測試: 為了確保程式碼的品質，完成了 GuessNumberViewModel 的單元測試。

* 廣告支援: 廣告為目前大部分免費應用營收很重要的項目之一，GuessNumber 串接 Firebase ADMOB，  
在每個頁面提供廣告橫幅，並提供廣告測試按鈕 (ADMOB TEST按鈕)。

* Youbike2.0: 作者自己常用的小功能，透過Retrofit2.0串接政府開源API，  
目前提供新北市的Youbike2.0即時狀態，以Retrofit搭配Kotlin Flow實作。
