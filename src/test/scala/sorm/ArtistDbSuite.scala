package sorm

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import samples._
import extensions.Extensions._

@RunWith(classOf[JUnitRunner])
class ArtistDbSuite extends FunSuite with ShouldMatchers {

  import ArtistDb._

  test("path with index"){
    Db.query[Artist]
      .filterEquals("names.value(1)", "Rolling Stones")
      .fetchOne()
      .get
      .names(Db.en)(1) should be === "Rolling Stones"
  }
  test("Offset"){
    pending
  }
  test("Limit"){
    pending
  }
  test("Ordering"){
    pending
  }
  test("Contains"){
    pending
  }
  test("Equality to unpersisted entity"){
    pending
  }
  test("Equality to persisted entity"){
    Db.query[Artist]
      .filterEquals("styles.item", Db.metal)
      .fetchAll()
      .flatMap{_.names.values.head}
      .toSet should be === Set("Metallica", "Godsmack")
  }
  test("Map, Set, Seq deep path"){
    Db.query[Artist]
      .filterEquals("styles.item.names.value.item", "Hard Rock")
      .fetchAll()
      .flatMap{_.names.values.head}
      .toSet should be === Set("Metallica", "Nirvana", "Godsmack")
  }
  test("Results have correct id property"){
    Db.query[Artist].fetchOne().map{_.id} should be === Some(1)
  }
  test("Query by id"){
    Db.query[Artist].filterEquals("id", 1).fetchOne()
      .map{_.names.values.head.head}.get should be === "Metallica"
    Db.query[Artist].filterEquals("id", 3).fetchOne()
      .map{_.names.values.head.head}.get should be === "Kino"
  }

}