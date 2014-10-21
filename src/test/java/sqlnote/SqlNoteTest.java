package sqlnote;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
        new SqlNote().setParameterNames(new ArrayList<>(Arrays.asList("aaa", "bbb", "aaa")));
    }
    
    @Test
    public void パラメータ名にドルマークが存在する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名に $, {, } は使用できません。");
        
        // exercise
        new SqlNote().setParameterNames(new ArrayList<>(Arrays.asList("aaa$", "bbb")));
    }
    
    @Test
    public void パラメータ名に波括弧開が存在する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名に $, {, } は使用できません。");
        
        // exercise
        new SqlNote().setParameterNames(new ArrayList<>(Arrays.asList("aaa", "b{bb")));
    }
    
    @Test
    public void パラメータ名に波括弧閉が存在する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名に $, {, } は使用できません。");
        
        // exercise
        new SqlNote().setParameterNames(new ArrayList<>(Arrays.asList("a}aa", "bbb")));
    }
}
