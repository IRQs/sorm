package sorm.test

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sext._, embrace._

import sorm._
import samples._
import org.joda.time.DateTime

@RunWith(classOf[JUnitRunner])
class DateTimeSupportSuite extends FunSuite with ShouldMatchers {
  import DateTimeSupportSuite._

  TestingInstances.instances(Set() + Entity[A]()).foreach{ case (db, dbId) =>
    val db = TestingInstance.h2(Entity[A]())

    //  time rounded to seconds (for mysql compatibility)
    val date = new DateTime(System.currentTimeMillis() / 1000 * 1000)
  
    val a1 = db.save(A(date))
    val a2 = db.save(A(date.plusHours(3)))
    val a3 = db.save(A(date.minusSeconds(5)))
    val a4 = db.save(A(date.minusSeconds(50)))
  
    test(dbId + " - Connection now()")(pending)
    test(dbId + " - Larger filter"){
      db.access[A].whereLarger("a", date.minusSeconds(1)).fetch()
        .should(
          contain(a1) and contain(a2) and not contain(a3) and not contain(a4)
        )
    }
    test(dbId + " - Smaller filter")(pending)
    test(dbId + " - Equal filter")(pending)
    test(dbId + " - In filter")(pending)
    test(dbId + " - Other filters fail")(pending)
  }
}
object DateTimeSupportSuite {
  case class A ( a : DateTime )
}