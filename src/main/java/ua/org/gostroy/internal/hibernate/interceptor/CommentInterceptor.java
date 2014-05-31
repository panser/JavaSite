package ua.org.gostroy.internal.hibernate.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.org.gostroy.domain.Comment;

import java.io.Serializable;

/**
 * Created by panser on 5/31/2014.
 */
public class CommentInterceptor extends EmptyInterceptor {
    private transient final Logger log = LoggerFactory.getLogger(getClass());

/*
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        fillSortProperty(entity,propertyNames,state);
        return true;
    }
*/

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        fillSortProperty(entity,propertyNames,state);
        return true;
    }

    private void fillSortProperty(Object entity, String[] propertyNames, Object[] state){
        log.trace("fillSortProperty() start");
        if(entity instanceof Comment){
            Comment comment = (Comment) entity;
            log.trace("fillSortProperty() comment.getId():" + comment.getId());
            Float sortId = (float)comment.getId();
            Comment tmpObject = comment;
            if(comment.getDepth() != 0 && comment.getDepth() != null){
                log.trace("fillSortProperty() in if");
                sortId = comment.getParent().getId() + sortId/(comment.getDepth()*10);
            }
            log.trace("fillSortProperty() after sortId:" + sortId);
//            comment.setSortId(sortId);
            for (int i = 0; i < propertyNames.length; i++) {
                if(propertyNames[i].equals("sortId")){
                    state[i] = sortId;
                }
            }
        }
    }
}
