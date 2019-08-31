import scala.io.Source

object ProcessTransactions extends App {

  //Define a case class Transaction which represents a transaction
  type TransactionId = String
  type AccountId = String
  type Day = Int
  type Category = String

  case class Transaction(transactionId: TransactionId,
                         accountId: AccountId,
                         transactionDay: Day,
                         category: Category,
                         transactionAmount: Double)


  //The full path to the file to import
  val fileName = "C:/Users/Gino/Desktop/transactions.txt"
  //The lines of the CSV file (dropping the first to remove the header)
  val transactionsLines = Source.fromFile(fileName).getLines().drop(1)

  //Here we split each line up by commas and construct Transactions
  val transactions: List[Transaction] = transactionsLines.map { line =>
    val split = line.split(',')
    Transaction(split(0), split(1), split(2).toInt, split(3), split(4).toDouble)
  }.toList


  //Question 1
  def totalAmountPerDay(transactions: List[Transaction]): Map[Day, Double] = {
    val groupedTransactions = transactions.groupBy(_.transactionDay)

    groupedTransactions.mapValues { transactions =>
      transactions.map(_.transactionAmount).sum
    }
  }

  //Question 2
  def averageAmountPerAccountAndCategory(transactions: List[Transaction]): Map[AccountId, Map[Category, Double]] = {
    val groupedByAcc = transactions.groupBy(_.accountId)
    groupedByAcc.mapValues { transactions =>
      transactions.groupBy(_.category).map { case (category: Category, transactions: List[Transaction]) =>
        category -> (transactions.map(_.transactionAmount).sum / transactions.size)
      }
    }
  }

  //Question 3
  case class WindowStats(day: Day,
                         max: Double = 0d,
                         avg: Double = 0d,
                         categoryTotals: List[(Category, Double)] = List())

  case class GroupedDailyTransactions(day: Day,
                                      accountId: AccountId,
                                      amountPerCategory: Map[Category, Double])

  def calculateWindowStats(transactions: List[Transaction],
                            windowSize: Int,
                            categoriesRequired: List[Category])
  : IndexedSeq[Map[AccountId, WindowStats]] = {
    val byDay      = transactions.groupBy(_.transactionDay)
    val lastDay    = byDay.keys.max
    val accounts   = transactions.groupBy(_.accountId).keySet
    val rangeDays  = windowSize + 1 to lastDay

    val results = rangeDays.map(day => {
      val windowDays         = (day - windowSize) until day
      val windowTransactions = windowDays.flatMap(d => byDay.getOrElse(d, List()))
      val byAccount          = windowTransactions.groupBy(_.accountId)
      val windowStats = byAccount.mapValues(windowTransactions => {
        val amounts    = windowTransactions.map(_.transactionAmount)
        val max        = amounts.max
        val avg        = amounts.sum / windowSize
        val byCategory = windowTransactions.groupBy(_.category)
        val totalByCategory = categoriesRequired.map(category =>
          category -> byCategory.getOrElse(category, List()).map(_.transactionAmount).sum
        )
        WindowStats(day, max, avg, totalByCategory)
      })
      //This is optional, just to give 0 values to missing accounts
      val missingAccounts = accounts -- windowStats.keySet
      val emptyStats      = missingAccounts.map(acc => acc -> WindowStats(day)).toMap
      windowStats ++ emptyStats
    })
    results
  }



  //Print results giving some format
  def showQuestion1(): Unit = {
    //Data sorted by day
    val data: List[(Day, Double)] =
      totalAmountPerDay(transactions).toList.sortBy(_._1)

    println("====Question 1====")
    data.foreach { case (day, totalAmount) =>
      println(s"Day: $day Total: $totalAmount")
    }
  }

  def showQuestion2(): Unit = {
    //Data sorted by account ID and category's average
    val data: List[(AccountId, List[(Category, Double)])] =
      averageAmountPerAccountAndCategory(transactions)
        .toList.sortBy(_._1).map(x => (x._1, x._2.toList.sortWith(_._2 > _._2)))

    println("====Question 2====")
    data.foreach { case (account, categories) =>
      println(s"Account ID: $account")
      categories.foreach { case (category, average) =>
        println(s"\tCategory: $category  Average: $average")
      }
    }
  }

  def showQuestion3(): Unit = {
    val categoriesRequired = List("AA", "CC", "FF")
    val data = calculateWindowStats(transactions, 5, categoriesRequired)

    println("====Question 3====")
    data.foreach { dailyMap =>
      dailyMap.foreach { case (account, stats) =>
        println(s"Account: $account | Day: ${stats.day} | Max: ${stats.max} | Avg: ${stats.avg} | Categories totals: ${stats.categoryTotals}")
      }
    }
  }

  showQuestion1()
  showQuestion2()
  showQuestion3()

}
