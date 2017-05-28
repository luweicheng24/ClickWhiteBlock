package com.gsww.www.clickwhiteblock;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import com.gsww.www.clickwhiteblock.bean.Person;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void seAppContext() throws Exception {
        // Context of the app under test.
        final Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.gsww.www.clickwhiteblock", appContext.getPackageName());
        Person p = new Person();
        p.setName("kobe");
        p.setPassword("123");
        p.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(appContext, "数据添加成功"+s, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(appContext, "数据添加失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
