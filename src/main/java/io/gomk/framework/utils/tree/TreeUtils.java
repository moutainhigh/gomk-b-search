package io.gomk.framework.utils.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils {
	public static <T extends TreeDto> List<T> getTree(List<T> sources){
        List<T> rootList =  getTreeRoot(sources);
        int len = rootList.size();
        for(int i=0;i<len;i++){
            getTreeChildNode(sources,rootList.get( i ));
        }
        return rootList;
    }

    /**
     * 分离出根节点
     * @return
     */
    private  static <T extends TreeDto> List<T> getTreeRoot(List<T> sources){
        List<T> rootList = new ArrayList<>(  );
        for(int i=sources.size()-1;i>=0;i--){
            if(sources.get( i ).getParentId().equals("0")){
                rootList.add( sources.remove( i ) );
            }
        }
        return rootList;
    }


    /**
     * 分离出子节点
     * @return
     */
    private  static <T extends TreeDto> void getTreeChildNode(List<T> sources,T parentNode){
        T t = null;
        for(int i=sources.size()-1;i>=0&i<sources.size();i--){
            if(sources.get( i ).getParentId().equals(parentNode.getId())){
                parentNode.getChildsList().add(sources.get( i ));
                t = sources.remove( i );
                getTreeChildNode(sources,t);
            }
        }
    }

}
