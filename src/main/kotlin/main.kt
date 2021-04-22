import java.text.SimpleDateFormat

val articles = mutableListOf<Article>()

fun readLineTrim() = readLine()!!.trim()
fun getArticleById(id : Int) :Article?{
    for (article in articles) {
        if (article.id == id) {
            return article
        }
    }
    return null
}

fun main() {
    println("게시물 관리 프로그램 실행")
    var articleLastId = 0


    loop@while (true){
        print("명령어 : ")
        val commend = readLineTrim()
        when{
            commend == "system exit" -> {
                println("프로그램을 종료합니다")
                break
            }
            commend.startsWith("article delete ") -> {
                val id = commend.trim().split(" ")[2].toInt()
                var articleToDelete = getArticleById(id)

                if (articleToDelete == null) {
                    println("${id}번 게시물은 존재하지 않습니다")
                    continue@loop
                }
                articles.remove(articleToDelete)
                println("${id}번 게시물을 삭제하였습니다")

            }
            commend ==  "article write" -> {
                val id = articleLastId +1
                val regDate = Util.getNowDateStr()
                val updateDate = Util.getNowDateStr()
                print("제목 : ")
                val title = readLineTrim()
                print("내용 : ")
                val body = readLineTrim()

                val article = Article(id, regDate, updateDate, title, body)

                articles.add(article)

                articleLastId = id
            }
            commend == "article list" -> {
                println("번호  /  작성일  /  수정일  /  제목")
                for (article in articles){
                    println("${article.id}  /  ${article.regDate}  /  ${article.updateDate}  /  ${article.title}")
                }
            }
            else -> {
                println("$commend 는 존재하지 않는 명령어입니다")
            }
        }
    }
    println("게시물 관리 프로그램 종료")
}
data class Article(val id : Int, val regDate: String, val updateDate: String, val title: String, val body: String){

}
object Util{
    fun getNowDateStr(): String {
        val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format1.format(System.currentTimeMillis())
    }

}
fun bubbleSort(numbers: MutableList<Int>) {
    val lastIndex = numbers.size -1
    var depth = lastIndex

    loop1@while (depth >= 1 ){
        for (i in 0 until depth){
            if(i == depth-1 && numbers[i] < numbers[i+1]){
                depth-=2
                continue@loop1
            }
            if(numbers[i] > numbers[i+1]){
                numbers[i] = numbers[i+1].also { numbers[i+1] = numbers[i] }
            }
        }
        depth--
    }
}