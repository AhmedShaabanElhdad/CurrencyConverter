package com.codegrow.feature.core

import com.codegrow.entity.Rate
import com.codegrow.entity.Symbol


class TestDataGenerator {

    companion object {
        fun generateSymbolItems() : HashMap<String,String> {
            val map:HashMap<String,String> = HashMap()
            map["AED"] = "United Arab Emirates Dirham"
            map["AFN"] = "Afghan Afghani"
            map["ALL"] = "Albanian Lek"
            return map
        }

        fun generateSymbolList() : List<Symbol> {
            val symbols:MutableList<Symbol>  = mutableListOf()
            symbols.add(Symbol("AED", "United Arab Emirates Dirham"))
            symbols.add(Symbol("ALL", "Albanian Lek"))
            symbols.add(Symbol("AFN", "Afghan Afghani"))
            return symbols
        }


        fun generateRatesList() : List<Rate> {
            val symbols:MutableList<Rate>  = mutableListOf()
            symbols.add(Rate("2012-05-01",1.280135,EUR = 1.296868,USD = 1.314491))
            symbols.add(Rate("2012-05-02",1.280135,EUR = 1.296868,USD = 1.314491))
            symbols.add(Rate("2012-05-03",1.280135,EUR = 1.296868,USD = 1.314491))
            return symbols
        }

        fun generateRateItems() : HashMap<String,Rate> {
            val map:HashMap<String,Rate> = HashMap()
            map["2012-05-01"] = Rate(CAD = 1.280135,EUR = 1.296868,USD = 1.314491)
            map["2012-05-02"] = Rate(CAD = 1.280135,EUR = 1.296868,USD = 1.314491)
            map["2012-05-03"] = Rate(CAD = 1.280135,EUR = 1.296868,USD = 1.314491)
            return map
        }
    }

}