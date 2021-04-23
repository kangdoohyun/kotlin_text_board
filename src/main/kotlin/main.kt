import java.text.SimpleDateFormat

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
                // command를 나눈다 [article, list, 검색어, 페이지번호]
                val commandBits = command.trim().split(" ")
                // page랑 searchKeyword를 임시값으로 초기화 한다
                var page = 1
                var searchKeyword = ""
                // 내가 command를 article list 제목_1 1 로 입력해서 검색어가 있다면 commandBits.size가 4이라 if가 실행
                // 내가 command를 article list 1 로 입력해서 검색어가 없다면 commandBits.size가 3이라 else if가 실행
                if (commandBits.size == 4) {
                    // searchKeyword 가 commandBits [article, list, 검색어, 페이지번호] 의 2번째 인덱스 즉 '검색어' 가 된다
                    searchKeyword = commandBits[2]
                    // page가 commandBits [article, list, 검색어, 페이지번호] 의 3번째 인덱스 즉 '페이지번호' 가 된다
                    page = commandBits[3].toInt()
                } else if (commandBits.size == 3) {
                    // commandBits가 3이면 검색어는 없기떄문에 페이지 번호가 2번째 인덱스가 된다
                    page = commandBits[2].toInt()
                }
                // 한페이지에 몇개의 개시물을 출력할지 정하는 변수
                val itemsCountInAPage = 10
                // 페이지가 몇이냐에 따라 몇개의 개시물을 건너뛰고 출력해야 할지 넘겨주는 변수
                val offsetCount = (page - 1) * itemsCountInAPage
                // getFilteredArticles 함수 실행
                val filteredArticles = getFilteredArticles(searchKeyword, offsetCount, itemsCountInAPage)

                println("번호 / 작성날짜 / 제목")
                // getFilteredArticles 함수에서 리턴된 filtered2Articles로 반복문을 돌려서 출력
                for (article in filteredArticles) {
                    println("${article.id} / ${article.regDate} / ${article.title}")
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

fun getFilteredArticles(searchKeyword: String, jumpIndex: Int, itemsInAPage: Int): MutableList<Article> {
    // articles 를 filtered1Articles 에 저장해 검색어가 없는경우에 모든 게시물을 상대로 리스팅할수있게 대비
    var filtered1Articles = articles
    // if문으로 searchKeyword 가 비어있는지 비어있지 않은지 검증 isNotEmpty 이기때문에 비어있지 않으면 true 리턴
    if (searchKeyword.isNotEmpty()) {
        // if문이 true 이면 전체개시물들을 저장해둔 filtered1Articles 에 비어있는 mutableList 를 새로 정의
        filtered1Articles = mutableListOf<Article>()

        for (article in articles) {
            // articles 에서 반복문으로 한개씩 끌어온 article 의 title 이랑 내가 입력한 검색어 searchKeyword가 포합되어있으면 true
            if (article.title.contains(searchKeyword)) {
                // 내가 검색한 검색어가 포함된 article을 걸러냈으니 걸러낸 결과물 즉 검색결과를 filtered1Articles에 add해서 추가
                filtered1Articles.add(article)
            }
        }
    }
    // 리스팅할 새로운 mutableList를정의
    val filtered2Articles = mutableListOf<Article>()

    val startIndex = filtered1Articles.lastIndex - jumpIndex
    var endIndex = startIndex - itemsInAPage + 1

    if (endIndex < 0) {
        endIndex = 0
    }
    // 시작 인덱스 부터 역순downTO 로 한개씩 감소시키며 종료 인덱스까지 인덱스를 하나씩 i 에 담아서 반복문 실행
    for (i in startIndex downTo endIndex) {
        // 위에서 새로 정의한 리스팅할 mutableList인 filtered2Articles에 위에서 검색어를 포함한 article을 add한 filtered1Articles를 add
        filtered2Articles.add(filtered1Articles[i])
    }

    return filtered2Articles
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