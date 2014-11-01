package sqlnote.domain;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static test.db.TestHelper.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SqlNoteTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    
    @Test
    public void 文字列型パラメータの型変換() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        String actual = note.convert("aaa", "String Message");
        
        // verify
        assertThat(actual, is("String Message"));
    }
    
    @Test
    public void 数値型パラメータの型変換() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        BigDecimal actual = note.convert("ccc", "12.4");
        
        // verify
        assertThat(actual, is(new BigDecimal("12.4")));
    }
    
    @Test
    public void 数値型パラメータに空文字を渡した場合は例外がスローされること() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("ccc は数値で指定してください。");
        
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        note.convert("ccc", "");
    }
    
    @Test
    public void 数値型パラメータにnullを渡した場合は例外がスローされること() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("ccc は数値で指定してください。");
        
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        note.convert("ccc", null);
    }
    
    @Test
    public void 数値型パラメータに数値以外の文字列を渡した場合は例外がスローされること() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("ccc は数値で指定してください。");
        
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        note.convert("ccc", "@@@");
    }
    
    @Test
    public void 日付型パラメータの型変換_年月日ハイフン繋ぎ() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        Date actual = note.convert("bbb", "2012-01-12");
        
        // verify
        assertThat(actual, is(date("2012-01-12")));
    }
    
    @Test
    public void 日付型パラメータの型変換_年月日スラッシュ繋ぎ() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        Date actual = note.convert("bbb", "2012/02/12");
        
        // verify
        assertThat(actual, is(date("2012-02-12")));
    }
    
    @Test
    public void 日付型パラメータの型変換_年月日ゼロ埋めなし() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        Date actual = note.convert("bbb", "2012/2/1");
        
        // verify
        assertThat(actual, is(date("2012-02-1")));
    }
    
    @Test
    public void 日付型パラメータの型変換_年月日時分秒ハイフン繋ぎ() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        Date actual = note.convert("bbb", "2012-02-01 12:13:14");
        
        // verify
        assertThat(actual, is(date("2012-02-1 12:13:14")));
    }
    
    @Test
    public void 日付型パラメータの型変換_年月日時分秒スラッシュ繋ぎ() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        Date actual = note.convert("bbb", "2012/02/01 12:13:14");
        
        // verify
        assertThat(actual, is(date("2012-02-1 12:13:14")));
    }
    
    @Test
    public void 日付型パラメータに空文字を渡すと例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("bbb は日付で指定してください。");
        
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        note.convert("bbb", "");
    }
    
    @Test
    public void 日付型パラメータにnullを渡すと例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("bbb は日付で指定してください。");
        
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        note.convert("bbb", null);
    }
    
    @Test
    public void 日付型パラメータに日付以外の文字列を渡すと例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("bbb は日付で指定してください。");
        
        SqlNote note = new SqlNote();
        note.setParameters(parameters(stringParameter("aaa"), dateParameter("bbb"), numberParameter("ccc")));
        
        // exercise
        note.convert("bbb", "not date text");
    }
    
    private Date date(String str) throws ParseException {
        return new Date(DateUtils.parseDate(str, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss").getTime());
    }
    
    @Test
    public void 新規作成時の名前() throws Exception {
        // exercise
        SqlNote note = new SqlNote();
        
        // verify
        assertThat(note.getTitle(), is("New SQL"));
    }
    
    @Test
    public void 新規作成時のSQLテンプレート() throws Exception {
        // exercise
        SqlNote note = new SqlNote();
        
        // verify
        assertThat(note.getSqlTemplate(), is("-- Enter a SQL"));
    }
    
    @Test
    public void タイトルに空文字を渡すと例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("タイトルは必ず指定してください。");
        
        // exercise
        new SqlNote().setTitle("");
    }
    
    @Test
    public void タイトルにnullを渡すと例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("タイトルは必ず指定してください。");
        
        // exercise
        new SqlNote().setTitle(null);
    }
    
    @Test
    public void SQLに空文字を渡すと例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("SQL は必ず指定してください。");
        
        // exercise
        new SqlNote().setSqlTemplate("");
    }
    
    @Test
    public void SQLにnullを渡すと例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("SQL は必ず指定してください。");
        
        // exercise
        new SqlNote().setSqlTemplate(null);
    }
    
    @Test
    public void パラメータ名が重複する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名が重複しています。");
        
        // exercise
        new SqlNote().setParameters(parameters(stringParameter("aaa"), stringParameter("bbb"), numberParameter("aaa")));
    }
    
    private List<SqlParameter> parameters(SqlParameter... parameters) {
        return new ArrayList<>(Arrays.asList(parameters));
    }
    
    @Test
    public void パラメータ名にドルマークが存在する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名に $, {, } は使用できません。");
        
        // exercise
        new SqlNote().setParameters(parameters(stringParameter("aaa$"), stringParameter("bbb")));
    }
    
    @Test
    public void パラメータ名に波括弧開が存在する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名に $, {, } は使用できません。");
        
        // exercise
        new SqlNote().setParameters(parameters(stringParameter("aaa$"), stringParameter("b{bb")));
    }
    
    @Test
    public void パラメータ名に波括弧閉が存在する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名に $, {, } は使用できません。");
        
        // exercise
        new SqlNote().setParameters(parameters(stringParameter("a}aa"), stringParameter("b{bb")));
    }
    
    @Test
    public void SQLテンプレートで未定義のパラメータが使用されていた場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("notExists はパラメータに定義されていません。");
        
        SqlNote note = new SqlNote();
        note.setSqlTemplate("${exists} ${notExists}");
        note.setParameters(parameters(stringParameter("exists")));
        
        // exercise
        note.verify();
    }
    
    @Test
    public void SQLテンプレートに使用されていないパラメータが定義されている場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("noUsed は SQL で使用されていません。");
        
        SqlNote note = new SqlNote();
        note.setSqlTemplate("${used}");
        note.setParameters(parameters(stringParameter("used"), numberParameter("noUsed")));
        
        // exercise
        note.verify();
    }
}
