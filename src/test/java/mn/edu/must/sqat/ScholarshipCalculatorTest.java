package mn.edu.must.sqat;

// ЛАБ 11 — тестийн багц.
// B1: starter-ийн СУЛ assertion-уудыг (assertNotNull, assertTrue(муж)) ЯГ УТГЫН
//     assertEquals болгосон — оролтууд нь starter-ийнхтэй ижил.
// B2: зааг утгын тестүүд нэмсэн (доод хэсэг).

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScholarshipCalculatorTest {

    private static final double DELTA = 1e-9;

    ScholarshipCalculator calc;

    @BeforeEach
    void setUp() { calc = new ScholarshipCalculator(); }

    // ---- letterGrade ----

    @Test
    void letterGrade_typicalScores_returnExactGrade() {
        assertEquals("A", calc.letterGrade(95));
        assertEquals("B", calc.letterGrade(85));
        assertEquals("C", calc.letterGrade(75));
        assertEquals("D", calc.letterGrade(65));
        assertEquals("F", calc.letterGrade(30));
    }

    @Test
    void letterGrade_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-1));
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(101));
    }

    // ---- gpa ----

    @Test
    void gpa_typicalScores_returnExactAverage() {
        assertEquals(3.5, calc.gpa(new double[]{95, 85}), DELTA);   // (4 + 3) / 2
        assertEquals(1.5, calc.gpa(new double[]{75, 65}), DELTA);   // (2 + 1) / 2
        assertEquals(0.0, calc.gpa(new double[]{40}), DELTA);       // F = 0
    }

    @Test
    void gpa_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(null));
        assertThrows(IllegalArgumentException.class, () -> calc.gpa(new double[]{}));
    }

    // ---- scholarship ----

    @Test
    void scholarship_typicalInputs_returnExactPercent() {
        assertEquals(100, calc.scholarship(3.9, 16, false));
        assertEquals(70, calc.scholarship(3.5, 13, false));
        assertEquals(50, calc.scholarship(3.1, 5, false));
        assertEquals(0, calc.scholarship(2.0, 1, false));
        assertEquals(0, calc.scholarship(4.0, 20, true));
    }

    @Test
    void scholarship_invalidInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.1, 10, false));
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.0, -1, false));
    }

    // =====================================================================
    // B2 — зааг утгын тестүүд
    // =====================================================================

    // ---- letterGrade: зааг бүрийн 2 тал (L20, L23-L26 "changed conditional boundary") ----

    @Test
    void letterGrade_boundaries_eachSideOfEveryLimit() {
        assertEquals("A", calc.letterGrade(100));
        assertEquals("A", calc.letterGrade(90));
        assertEquals("B", calc.letterGrade(89.99));
        assertEquals("B", calc.letterGrade(80));
        assertEquals("C", calc.letterGrade(79.99));
        assertEquals("C", calc.letterGrade(70));
        assertEquals("D", calc.letterGrade(69.99));
        assertEquals("D", calc.letterGrade(60));
        assertEquals("F", calc.letterGrade(59.99));
        assertEquals("F", calc.letterGrade(0));
    }

    @Test
    void letterGrade_justOutsideRange_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(-0.01));
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(100.01));
    }

    // ---- gpa: switch-ийн бүх үсгэн дүн тус тусдаа ----

    @Test
    void gpa_eachLetterGrade_mapsToExactPoints() {
        assertEquals(4.0, calc.gpa(new double[]{95}), DELTA);
        assertEquals(3.0, calc.gpa(new double[]{85}), DELTA);
        assertEquals(2.0, calc.gpa(new double[]{75}), DELTA);
        assertEquals(1.0, calc.gpa(new double[]{65}), DELTA);
        assertEquals(0.0, calc.gpa(new double[]{30}), DELTA);
        assertEquals(2.0, calc.gpa(new double[]{95, 85, 75, 65, 30}), DELTA);   // (4+3+2+1+0)/5
    }

    // ---- scholarship: 3.8/15, 3.4/12, 3.0 заагууд — && -ийн нэг тал хангагдаж нөгөө нь үгүй ----

    @Test
    void scholarship_boundary_3_8_and_15() {
        assertEquals(100, calc.scholarship(3.8, 15, false));
        assertEquals(70, calc.scholarship(3.79, 15, false));    // голч дутуу (кредит хангасан)
        assertEquals(70, calc.scholarship(3.8, 14, false));     // кредит дутуу (голч хангасан)
    }

    @Test
    void scholarship_boundary_3_4_and_12() {
        assertEquals(70, calc.scholarship(3.4, 12, false));
        assertEquals(50, calc.scholarship(3.39, 12, false));    // голч дутуу
        assertEquals(50, calc.scholarship(3.4, 11, false));     // кредит дутуу
    }

    @Test
    void scholarship_boundary_3_0() {
        assertEquals(50, calc.scholarship(3.0, 0, false));
        assertEquals(0, calc.scholarship(2.99, 0, false));
    }

    // ---- scholarship: оролтын хязгаар (L48) — gpa 0 ба 4, credits 0 хүчинтэй ----

    @Test
    void scholarship_inputLimits_areValid() {
        assertEquals(0, calc.scholarship(0.0, 0, false));       // gpa = 0, credits = 0
        assertEquals(100, calc.scholarship(4.0, 15, false));    // gpa = 4
    }

    @Test
    void scholarship_justOutsideInputLimits_throws() {
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(-0.01, 10, false));
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(4.01, 10, false));
        assertThrows(IllegalArgumentException.class, () -> calc.scholarship(3.0, -1, false));
    }
}
