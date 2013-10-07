(ns pcgplay.core
  (:require [instaparse.core :as insta]
            )
  )

(def testlines (apply str
                      (interleave
                       '(
                         "PCGVERSION:2.0"
                         "AGE:"
                         "SKILL:Appraise|OUTPUTORDER:1|"
                         "CLASS:Sorcerer|LEVEL:6|HITDICE:3|SPELLSLOTS:0,6,5,3"
                         "EQUIPMENT:Longbow +2|BASEITEM:[NAME:Longbow|CUSTOM:EQMOD$MAGIC.ENH.PLUS2]"
                         "WEAPONPROF:[WEAPON:Longbow|WEAPON:Claws|WEAPON:Heavy Mace]"
                         "HAIR:"
                         )
                       (repeat "\n")
                       )
                )
  )

testlines

(def char-parser
  (insta/parser
   "
   character = line*

   kvsep = ':'
   kvdelim = '|'

   <validchar> = #'[^:|\\[\\]]'
   key = validchar+
   val = validchar*

   linekey = key

   kv = key <kvsep> val
   listkv = key <kvsep> listval

   listval = <'['> (kv | <kvdelim>)+ <']'>
   mapval = key (<kvdelim> (kv | listkv)?)+

   line = key <kvsep> (val | mapval | listval) <'\n'>
   "
   )
  )

char-parser

(def parsetransform
  {
   :key str
   :val str
   }
  )

(insta/transform parsetransform
                 (char-parser testlines)
                 )

