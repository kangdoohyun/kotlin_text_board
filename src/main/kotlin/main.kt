
import java.text.SimpleDateFormat


fun readLineTrim() = readLine()!!.trim()

fun main() {
    println("== 게시판 관리 프로그램 시작 ==")

    var articlesLastId = 0
    val articles = mutableListOf<Article>()

    loop@while(true){
        print("명령어 입력 : ")
        val command = readLineTrim()

        when (command){
            "system exit" -> {
                println("시스템을 종료합니다")
                break@loop
            }
            "article write" -> {
                val id = articlesLastId +1
                val regDate = Util.getNowDateStr()
                val updateDate = Util.getNowDateStr()

                print("제목 : ")
                val title = readLineTrim()

                print("내용 : ")
                val body = readLineTrim()

                var article = Article(id, regDate, updateDate, title, body)
                articles.add(article)

                println("${id}번 게시물이 작성되었습니다")

                articlesLastId = id
            }
            "article list" -> {
                println("번호  /  제목  /  작성일  /  수정일")
                for (article in articles){
                    println("${article.id}  /  ${article.title}  /  ${article.regDate}  /  ${article.updateDate}")
                }
            }
            else -> {
                println("$command 는 존재하지 않는 명령어 입니다")
            }
        }
    }
    println("== 게시판 관리 프로그램 종료 ==")
}
data class Article(val id : Int, val regDate : String, val updateDate : String, val title : String, val body : String){

}
object Util{
    fun getNowDateStr(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(System.currentTimeMillis())
    }
}