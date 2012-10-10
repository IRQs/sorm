package sorm.mappings

import sext.Sext._
import sorm._
import core._
import reflection.Reflection

class SetMapping
  ( val reflection : Reflection,
    val membership : Option[Membership],
    val settings : Map[Reflection, EntitySettings],
    val driver : Driver )
  extends SlaveTableMapping
  {

    lazy val item = Mapping( reflection.generics(0), Membership.SetItem(this), settings, driver )
    lazy val primaryKeyColumns = masterTableColumns :+ hashColumn
    lazy val hashColumn = ddl.Column( "h", ddl.ColumnType.Integer )
    lazy val mappings = item +: Stream()
    def parseRows ( rows : Stream[String => Any] )
      = rows.map(item.valueFromContainerRow).toSet

    override def update ( value : Any, masterKey : Stream[Any] ) {
      driver.delete(tableName, masterTableColumnNames zip masterKey)
      insert(value, masterKey)
    }

    override def insert ( value : Any, masterKey : Stream[Any] ) {
      item match {
        case item : MasterTableMapping =>
          value.asInstanceOf[Set[_]].view
            .zipWithIndex.foreach{ case (v, i) =>
              val values = (primaryKeyColumnNames zip (masterKey :+ i)) ++: item.valuesForContainerTableRow(v)
              driver.insert(tableName, values)
            }
        case item =>
          value.asInstanceOf[Set[_]].view
            .zipWithIndex.foreach{ case (v, i) =>
              val pk = masterKey :+ i
              driver.insert(tableName, primaryKeyColumnNames zip pk)
              item.insert(v, pk)
            }
      }
    }

    def valuesForContainerTableRow ( value : Any ) = Stream()

  }
