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
                print("제목 : ")
                val title = readLineTrim()
                print("내용 : ")
                val body = readLineTrim()

                var article = Article(id, title, body)
                println("${id}번 게시물이 작성되었습니다")
                articlesLastId = id
            }
        }
    }
    println("== 게시판 관리 프로그램 종료 ==")
}
data class Article(val id : Int, val title : String, val body : String){

}