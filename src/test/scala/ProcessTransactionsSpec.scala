import ProcessTransactions._
import org.scalatest.FunSuite

class ProcessTransactionsSpec extends FunSuite with TransactionFixture {

  test("getTotalPerDay") {
    val expectedResult: Map[Day, Double] =
      Map(5 -> 85.0, 1 -> 66.0, 6 -> 54.0, 2 -> 48.0, 3 -> 74.0, 8 -> 25.4, 4 -> 53.5)
    assert(totalAmountPerDay(allTheTransactions) == expectedResult)
  }

  test("averageAmountPerAccountAndCategory") {
    val expectedResult: Map[AccountId, Map[Category, Double]] =
      Map("A2" ->
        Map(
          "AA" -> 25.0,
          "EE" -> 26.8,
          "BB" -> 26.0,
          "FF" -> 28.333333333333332),
        "A1" ->
          Map(
            "BB" -> 23.666666666666668,
            "AA" -> 20.0,
            "EE" -> 26.0,
            "CC" -> 23.0,
            "DD" -> 24.75)
      )
    assert(averageAmountPerAccountAndCategory(allTheTransactions) == expectedResult)
  }

  test("calculateWindowStats") {
    val expectedResult = IndexedSeq(
      Map(
        "A2" -> WindowStats(6,30.0,33.0,List(("AA",25.0), ("CC",0.0), ("FF",59.0))),
        "A1" -> WindowStats(6,26.0,32.3,List(("AA",20.0), ("CC",23.0), ("FF",0.0)))),
      Map(
        "A2" -> WindowStats(7,30.0,33.2,List(("AA",0.0), ("CC",0.0), ("FF",85.0))),
        "A1" -> WindowStats(7,28.0,29.7,List(("AA",0.0), ("CC",23.0), ("FF",0.0)))),
      Map(
        "A2" -> WindowStats(8,30.0,28.0,List(("AA",0.0), ("CC",0.0), ("FF",85.0))),
        "A1" -> WindowStats(8,28.0,25.3,List(("AA",0.0), ("CC",23.0), ("FF",0.0))))
    )
      assert(calculateWindowStats(allTheTransactions, 5, List("AA", "CC", "FF")) == expectedResult)
  }

}