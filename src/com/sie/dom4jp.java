package com.sie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class dom4jp {

	public static void main(String[] args) throws DocumentException, IOException {
		JSONObject json = new JSONObject();
		JSONArray datas = new JSONArray();
        SAXReader reader = new SAXReader();  
        InputStream inputStream = dom4jp.class.getClassLoader().getResourceAsStream("Mtime_movie.xml");
        Document document = reader.read(inputStream); 
        Element root = document.getRootElement();
        List<Element> films = root.elements("Film");
        List<String> data = new ArrayList<>();
        for (Element element : films) {
        	JSONObject film = new JSONObject();
        	datas.add(film);
			String filmNo = element.elementText("FilmNo");
			String filmName = element.elementText("FilmName");
			String poster = element.elementText("Poster");
			film.put("FilmNo", filmNo);
			film.put("FilmName", filmName);
			film.put("Poster", poster);
			
			System.out.println(filmNo+":"+filmName+":"+poster);
			Element stills = element.element("Stills");
			List<String> list = new ArrayList<>();
			film.put("剧照", list);
			json.put("data", datas);
			if(stills == null){
				continue;
			}
			List<Element> stills1 = stills.elements("Still");
			if(stills1 == null){
				continue;
			}
			for (Element element2 : stills1) {
				list.add(element2.elementText("StillsNo"));
				System.out.println(element2.elementText("StillsNo"));
			}
		}
        JSONArray array = json.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			String filmName = jsonObject.getString("FilmName");
			System.out.println(filmName);
		}
        
        System.out.println(JSON.toJSONString(json, SerializerFeature.DisableCircularReferenceDetect));
        createXML();
	}
	public static void createXML() throws IOException{
		Document document = DocumentHelper.createDocument();
        Element root = document.addElement("root");

        Element author1 = root.addElement("author")
            .addAttribute("name", "James")
            .addAttribute("location", "UK")
            .addText("James Strachan");

        Element author2 = root.addElement("author")
            .addAttribute("name", "Bob")
            .addAttribute("location", "US");
        author2.addElement("film").addText("小A");
        author2.addElement("film").addText("小A");
        author2.addElement("film").addText("小A");
        File tmp = new File("src","abc.xml");
        tmp.createNewFile();
        OutputStream oStream = new FileOutputStream(tmp);
        OutputFormat xmlFormat = new OutputFormat();  
        xmlFormat.setEncoding("UTF-8"); 
        // 设置换行 
        xmlFormat.setNewlines(true); 
        // 生成缩进 
        xmlFormat.setIndent(true); 
        // 使用4个空格进行缩进, 可以兼容文本编辑器 
        xmlFormat.setIndent("    "); 
        XMLWriter writer = new XMLWriter(oStream,xmlFormat);
        writer.write(document);
        writer.close();
	}

}
