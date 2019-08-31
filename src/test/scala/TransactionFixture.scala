import ProcessTransactions._

trait TransactionFixture {
  val transaction1  = Transaction("T1", "A1", 1, "AA", 20.0d)
  val transaction2  = Transaction("T2", "A1", 1, "BB", 21.0d)
  val transaction3  = Transaction("T3", "A1", 2, "BB", 22.0d)
  val transaction4  = Transaction("T4", "A1", 3, "CC", 23.0d)
  val transaction5  = Transaction("T5", "A1", 3, "DD", 24.0d)
  val transaction6  = Transaction("T6", "A1", 4, "DD", 25.5d)
  val transaction7  = Transaction("T7", "A1", 5, "EE", 26.0d)
  val transaction8  = Transaction("T8", "A1", 6, "BB", 28.0d)

  val transaction9  = Transaction("T9", "A2", 1, "AA", 25.0d)
  val transaction10 = Transaction("T10", "A2", 2, "BB", 26.0d)
  val transaction11 = Transaction("T11", "A2", 3, "EE", 27.0d)
  val transaction12 = Transaction("T12", "A2", 4, "EE", 28.0d)
  val transaction13 = Transaction("T13", "A2", 5, "FF", 30.0d)
  val transaction14 = Transaction("T14", "A2", 5, "FF", 29.0d)
  val transaction15 = Transaction("T15", "A2", 6, "FF", 26.0d)
  val transaction16 = Transaction("T16", "A2", 8, "EE", 25.4d)


  val allTheTransactions = List(
    transaction1,
    transaction2,
    transaction3,
    transaction4,
    transaction5,
    transaction6,
    transaction7,
    transaction8,
    transaction9,
    transaction10,
    transaction11,
    transaction12,
    transaction13,
    transaction14,
    transaction15,
    transaction16
  )
}
