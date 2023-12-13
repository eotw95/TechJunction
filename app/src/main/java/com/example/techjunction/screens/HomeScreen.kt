package com.example.techjunction.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import com.example.techjunction.constants.HATENA
import com.example.techjunction.constants.QIITA
import com.example.techjunction.constants.TOP
import com.example.techjunction.constants.ZENN
import com.example.techjunction.constants.services
import com.example.techjunction.screens.component.ArticleSection
import com.example.techjunction.screens.component.CategoryTab
import com.example.techjunction.screens.component.Header

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onClick: (String) -> Unit
) {
    Column {
        Header()
        CategoryTab()

        // Todo: 各サービスの記事一覧ページを独立させて、HorizontalPagerで横スクロールできるようにする
        //  →　独立させずに分岐させる、独立させると似たようなコードが重複する
        HorizontalPager(pageCount = services.size) {
            when (it) {
                0 -> ArticleSection(onClick, TOP)
                1 -> ArticleSection(onClick, QIITA)
                2 -> ArticleSection(onClick, ZENN)
                3 -> ArticleSection(onClick, HATENA)
            }
        }
    }
}