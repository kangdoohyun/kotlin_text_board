import java.text.SimpleDateFormat
import kotlin.math.ceil

fun main() {
    println("게시물 관리 프로그램 실행")
    makeTestArticles()

    loop@while (true){

        print("명령어 : ")
        val command = readLineTrim()
        when{
            command == "system exit" -> {
                println("프로그램을 종료합니다")
                break
            }
            command.startsWith("article delete ") -> {
                val id = command.trim().split(" ")[2].toInt()
                val articleToDelete = getArticleById(id)

                if (articleToDelete == null) {
                    println("${id}번 게시물은 존재하지 않습니다")
                    continue@loop
                }
                articles.remove(articleToDelete)
                println("${id}번 게시물을 삭제하였습니다")

            }
            command.startsWith("article modify ") -> {
                val id = command.trim().split(" ")[2].toInt()
                var articleToModify = getArticleById(id)

                if (articleToModify == null){
                    println("${id}번 게시물은 존재하지 않습니다")
                    continue@loop
                }
                print("수정할 제목 :")
                articleToModify.title = readLineTrim()
                print("수정할 내용 :")
                articleToModify.body = readLineTrim()
                articleToModify.updateDate = Util.getNowDateStr()
                println("${id}번 게시물을 수정하였습니다")
            }
            command.startsWith("article detail ") -> {
                val id = command.trim().split(" ")[2].toInt()
                var articleToDetail = getArticleById(id)

                if (articleToDetail == null){
                    println("${id}번 게시물은 존재하지 않습니다")
                    continue@loop
                }
                println("번호 : ${articleToDetail.id}")
                println("작성 날짜 : ${articleToDetail.regDate}")
                println("수정 날짜 : ${articleToDetail.updateDate}")
                println("제목 : ${articleToDetail.title}")
                println("내용 : ${articleToDetail.body}")
            }
            command ==  "article write" -> {
                print("제목 : ")
                val title = readLineTrim()
                print("내용 : ")
                val body = readLineTrim()

                val id = addArticle(title, body)
                println("${id}번 게시물이 작성되었습니다")
            }
            command.startsWith("article list ") -> {
                val currentPage = command.split(" ")[2].toInt()
                val itemsInAPage = 10
                val jumpIndex = (currentPage - 1) * itemsInAPage

                val articles = getFilteredArticles(jumpIndex, itemsInAPage)

                println("번호 /         작성일         /         수정일         /  제목")
                for (article in articles){
                    println("${article.id}  /  ${article.regDate}  /  ${article.updateDate}  /  ${article.title}")
                }
            }
            else -> {
                println("$command 는 존재하지 않는 명령어입니다")
            }
        }
    }
    println("게시물 관리 프로그램 종료")
}


/* 게시물 관련 시작 */
// 가장 마지막에 입력된 게시물 번호
var articlesLastId = 0

val articles = mutableListOf<Article>()

fun getFilteredArticles(jumpIndex: Int, itemsInAPage: Int): MutableList<Article> {
    val startIndex = articles.lastIndex - jumpIndex
    val endIndex = startIndex - itemsInAPage + 1

    val filteredArticles = mutableListOf<Article>()
    for (i in startIndex downTo endIndex){
        filteredArticles.add(articles[i])
    }
    return filteredArticles
}

fun getArticleById(id: Int): Article? {
    for (article in articles) {
        if (article.id == id) {
            return article
        }
    }

    return null
}

fun addArticle(title: String, body: String): Int {
    val id = articlesLastId + 1
    val regDate = Util.getNowDateStr()
    val updateDate = Util.getNowDateStr()

    val article = Article(id, regDate, updateDate, title, body)
    articles.add(article)

    articlesLastId = id

    return id
}

fun makeTestArticles() {
    for (id in 1..100) {
        val title = "제목_$id"
        val body = "내용_$id"

        addArticle(title, body)
    }
}

data class Article(val id: Int, val regDate: String, var updateDate: String, var title: String, var body: String
)
/* 게시물 관련 끝 */

/* 유틸관련 시작 */
fun readLineTrim() = readLine()!!.trim()

object Util {
    fun getNowDateStr(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return dateFormat.format(System.currentTimeMillis())
    }
}
/* 유틸관련 끝 */