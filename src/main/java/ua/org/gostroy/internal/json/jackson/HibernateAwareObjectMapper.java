package ua.org.gostroy.internal.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 * Created by panser on 6/12/2014.
 */

public class HibernateAwareObjectMapper extends ObjectMapper {
    public HibernateAwareObjectMapper() {
        registerModule(new Hibernate4Module());
        registerModule(new JaxbAnnotationModule());
    }
}