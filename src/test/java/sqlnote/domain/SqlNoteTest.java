package sqlnote.domain;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static test.db.TestHelper.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sqlnote.domain.IllegalParameterException;
import sqlnote.domain.SqlNote;
import sqlnote.domain.SqlParameter;

public class SqlNoteTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void 新規作成時の名前() throws Exception {
        // exercise
        SqlNote note = new SqlNote();
        
        // verify
        assertThat(note.getTitle(), is("新規SQL"));
    }
    
    @Test
    public void 新規作成時のSQLテンプレート() throws Exception {
        // exercise
        SqlNote note = new SqlNote();
        
        // verify
        assertThat(note.getSqlTemplate(), is("-- SQL を入力してください"));
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
