package fr.insalyon.creatis.vip.application.integrationtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.insalyon.creatis.vip.application.client.bean.Application;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.integrationtest.database.BaseSpringIT;
import fr.insalyon.creatis.vip.core.server.SpringInternalApiConfig;

@ContextConfiguration(classes = { SpringInternalApiConfig.class })
public class ApplicationControllerIT extends BaseSpringIT {
    
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private User adminUser;
    private User developperUser;
    private User basicUser;

    @BeforeEach
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .defaultRequest(MockMvcRequestBuilders.get("/").servletPath("/internal"))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        mapper = new ObjectMapper();

        adminUser = createUser(emailUser1, UserLevel.Administrator);
        developperUser = createUser(emailUser2, UserLevel.Developer);
        basicUser = createUser(emailUser3, UserLevel.Beginner);
    }

    @Test
    public void add() throws Exception {
        Application app = new Application("super_app", "les applications sont vraiment belles");

        // not the rights
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(basicUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                .andDo(print())
                    .andExpect(status().is4xxClientError()); // waiting for good API Exception

        // at least developer
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(developperUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                .andDo(print())
                    .andExpect(status().isOk()); // waiting for good API Exception

        // or admin
        // here it perform an update since application already exist
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                .andDo(print())
                    .andExpect(status().isOk()); // waiting for good API Exception
    }

    @Test
    public void remove() throws Exception {
        Application app = new Application("super_app", "les applications sont vraiment belles");

        // create app first
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                .andDo(print())
                    .andExpect(status().isOk());

        // not the rights
        mockMvc.perform(delete("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(basicUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app.getName())))
                .andDo(print())
                    .andExpect(status().is4xxClientError()); // waiting for good API Exception
        
        // developer account cannot do that
        mockMvc.perform(delete("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(developperUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app.getName())))
                .andDo(print())
                    .andExpect(status().is4xxClientError()); // waiting for good API Exception
    
        // good one and admin!
        mockMvc.perform(delete("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app.getName())))
                .andDo(print())
                    .andExpect(status().isOk()); // waiting for good API Exception
    }

    @Test
    public void update() throws Exception {
        Application app = new Application("super_app", "les applications sont vraiment belles");

        // create app
        mockMvc.perform(post("/internal/applications")
            .with(getUserSecurityMock(adminUser)).with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(app)))
                    .andExpect(status().isOk());

        app.setCitation("les applications sont vraimen moches");

        // update app wrong matching ids
        mockMvc.perform(put("/internal/applications/not_good_name")
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(8009));

        // do update
        mockMvc.perform(put("/internal/applications/" + app.getName())
            .with(getUserSecurityMock(adminUser))
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(app)))
                .andDo(print())
                    .andExpect(status().isOk());
    }

    // @Test
    // public void get() throws Exception {
    // }

    // @Test
    // public void getList() throws Exception {
    // }
}