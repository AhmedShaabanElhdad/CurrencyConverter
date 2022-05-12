package com.codegrow.domain.core

import com.codegrow.entity.Rate


class TestDataGenerator {

    companion object {
        fun generateSymbolItems() : HashMap<String,String> {
            val map:HashMap<String,String> = HashMap()
            map["AED"] = "United Arab Emirates Dirham"
            map["AFN"] = "Afghan Afghani"
            map["ALL"] = "Albanian Lek"
            map["AMD"] = "Armenian Dram"
            return map
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