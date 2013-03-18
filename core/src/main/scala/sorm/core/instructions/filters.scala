package sorm.core.instructions

import sorm.core.subRef._

sealed trait Filters
  [ Entity, Input ]

case class FiltersNil
  [ Entity ]
  extends Filters[ Entity, Unit ]

case class Fork
  [ Entity, HeadInput, TailInput ]
  ( head : Filters[ Entity, HeadInput ],
    tail : Filters[ Entity, TailInput ] )
  extends Filters[ Entity, (HeadInput, TailInput) ]

case class Comparison
  [ Entity, ValueOutput, ValueInput, TailInput ]
  ( subRef : SubRef[ Entity, ValueOutput ],
    value : Reference[ Entity, ValueInput, ValueOutput ],
    operator : Operator,
    negative : Boolean,
    tail : Filters[ Entity, TailInput ] )
  extends Filters[ Entity, (ValueInput, TailInput) ]


sealed trait Reference
  [ Entity, Input, Output ]

case class ReferenceSelect
  [ Entity, Input, Output ]
  ( select : Select[ Entity, Input, Output ] )
  extends Reference[ Entity, Input, Output ]

case class ReferenceValue
  [ Entity, Value ]
  extends Reference[ Entity, Value, Value ]


sealed trait Operator
case object Equal extends Operator
case object Larger extends Operator
case object Smaller extends Operator
case object Like extends Operator
case object Regex extends Operator
case object In extends Operator
case object Contains extends Operator
/**
 * Makes part of a collection
 */
case object Constitutes extends Operator
/**
 * Includes a collection
 */
case object Includes extends Operator
case object HasSize extends Operator

