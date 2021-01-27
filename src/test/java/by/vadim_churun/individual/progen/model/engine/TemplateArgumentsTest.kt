package by.vadim_churun.individual.progen.model.engine

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class TemplateArgumentsTest {
    // ================================================================================
    // Strings:

    @Test
    fun getStringPositive() {
        val args = MutableTemplateArguments()
        args.add("myKey", "myValue")
        val actual = args.getString("myKey")
        Assertions.assertEquals("myValue", actual)
    }

    @Test
    fun getStringNegative() {
        val args = MutableTemplateArguments()
        args.add("myKey", "myValue")

        Assertions.assertThrows(IllegalStateException::class.java) {
            args.getString("anotherKey")
        }
    }

    @Test
    fun getStringCase() {
        val args = MutableTemplateArguments()
        args.add("myKey", "myValue")

        Assertions.assertThrows(IllegalStateException::class.java) {
            args.getString("mykey")
        }
    }

    @Test
    fun getStringDefault() {
        val args = MutableTemplateArguments()
        args.add("specifiedKey", "desiredValue")
        val actual = args.getString("anotherKey", "defaultValue")
        Assertions.assertEquals("defaultValue", actual)
    }


    // ================================================================================
    // Numbers:

    @Test
    fun getInt() {
        val args = MutableTemplateArguments()
        args.add("myInt", "17305")
        val actual = args.getInt("myInt")
        Assertions.assertEquals(17305, actual)
    }

    @Test
    fun getIntBadFormat() {
        val args = MutableTemplateArguments()
        args.add("myInt", "Not an integer")
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            args.getInt("myInt")
        }
    }

    @Test
    fun getIntDefault() {
        val args = MutableTemplateArguments()
        args.add("myInt", "Not an integer")
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            args.getInt("myInt", 42)
        }
    }

    @Test
    fun getIntSpaces() {
        val args = MutableTemplateArguments()
        args.add("myInt", "   73 041  522 ")
        val actual = args.getInt("myInt")
        Assertions.assertEquals(73_041_522, actual)
    }

    @Test
    fun getIntUnderscores() {
        val args = MutableTemplateArguments()
        args.add("myInt", "73__041__522")
        val actual = args.getInt("myInt")
        Assertions.assertEquals(73_041_522, actual)
    }

    @Test
    fun getIntNegative() {
        val args = MutableTemplateArguments()
        args.add("negativeInt", "-38")
        val actual = args.getInt("negativeInt")
        Assertions.assertEquals(-38, actual)
    }

    @Test
    fun getIntMixed() {
        val args = MutableTemplateArguments()
        args.add("mixedInt", "  - 74_870 ")
        val actual = args.getInt("mixedInt")
        Assertions.assertEquals(-74_870, actual)
    }

    @Test
    fun getLong() {
        val args = MutableTemplateArguments()
        args.add("myLong", "18 364 293 714")
        val actual = args.getLong("myLong")
        Assertions.assertEquals(18_364_293_714L, actual)
    }

    @Test
    fun getIntTooBig() {
        val args = MutableTemplateArguments()
        args.add("myInt", "18 364 293 714")

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            args.getInt("myInt")
        }
    }

    @Test
    fun getFloatPoint() {
        val args = MutableTemplateArguments()
        args.add("myFloat", "18.52")
        val actual = args.getFloat("myFloat")
        Assertions.assertEquals(18.52f, actual)
    }

    @Test
    fun getFloatComma() {
        val args = MutableTemplateArguments()
        args.add("myFloat", "18,52")
        val actual = args.getFloat("myFloat")
        Assertions.assertEquals(18.52f, actual)
    }

    @Test
    fun getFloatComplex() {
        val args = MutableTemplateArguments()
        args.add("complexFloat", "  - 173_854 , 15_30 ")
        val actual = args.getFloat("complexFloat")
        Assertions.assertEquals(-173_854.1530f, actual)
    }

    @Test
    fun getIntAsFloat() {
        val args = MutableTemplateArguments()
        args.add("int", "13")
        val actual = args.getFloat("int")
        Assertions.assertEquals(13f, actual)
    }

    @Test
    fun getDouble() {
        val args = MutableTemplateArguments()
        args.add("double", "128500.875")
        val actual = args.getDouble("double")
        Assertions.assertEquals(128500.875, actual)
    }


    // ================================================================================
    // Booleans:

    @Test
    fun getTrue() {
        val args = MutableTemplateArguments()
        args.add("b1", "true")
        args.add("b2", "yes")
        args.add("b3", "on")

        val booleans = listOf(
            args.getBoolean("b1"),
            args.getBoolean("b2"),
            args.getBoolean("b3")
        )
        for(b in booleans) {
            Assertions.assertTrue(b)
        }
    }

    @Test
    fun getFalse() {
        val args = MutableTemplateArguments()
        args.add("b1", "false")
        args.add("b2", "no")
        args.add("b3", "off")

        val booleans = listOf(
            args.getBoolean("b1"),
            args.getBoolean("b2"),
            args.getBoolean("b3")
        )
        for(b in booleans) {
            Assertions.assertFalse(b)
        }
    }

    @Test
    fun booleansNoise() {
        val args = MutableTemplateArguments()
        args.add("b1", "  truE ")
        args.add("b2", " oFF  ")

        val b1 = args.getBoolean("b1")
        val b2 = args.getBoolean("b2")
        Assertions.assertTrue(b1)
        Assertions.assertFalse(b2)
    }


    // ================================================================================
    // Enums:

    private enum class TestEnum {
        SIMPLE,
        COMPLEX_VALUE
    }

    @Test
    fun getEnumSimple() {
        testEnum("simple", TestEnum.SIMPLE)
    }

    @Test
    fun getComplexSpace() {
        testEnum("complex value", TestEnum.COMPLEX_VALUE)
    }

    @Test
    fun getComplexUnderscore() {
        testEnum("complex_value", TestEnum.COMPLEX_VALUE)
    }

    @Test
    fun getComplexHyphen() {
        testEnum("complex-value", TestEnum.COMPLEX_VALUE)
    }

    @Test
    fun getComplexMixed() {
        testEnum("_ _ CompleX  _- vALUe  --", TestEnum.COMPLEX_VALUE)
    }

    @Test
    fun getComplexInvalid() {
        testEnum("simple complex value", null)
    }


    private fun testEnum(input: String, expected: TestEnum?) {
        val args = MutableTemplateArguments()
        args.add("enum", input)

        if(expected == null) {
            Assertions.assertThrows(IllegalArgumentException::class.java) {
                args.getEnumConstant("enum", TestEnum::class.java)
            }
        } else {
            val actual = args.getEnumConstant("enum", TestEnum::class.java)
            Assertions.assertEquals(expected, actual)
        }
    }

}