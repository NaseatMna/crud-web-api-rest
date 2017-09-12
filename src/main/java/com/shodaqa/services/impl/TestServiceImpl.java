package com.shodaqa.services.impl;

import com.shodaqa.models.Test;
import com.shodaqa.repositories.BaseRepositories;
import com.shodaqa.repositories.TestRepo;
import com.shodaqa.services.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Naseat_PC on 9/12/2017.
 */
@Service
public class TestServiceImpl extends BaseServicesImpl<Test, Long> implements TestService {
    @Resource(type = TestRepo.class)

    public void setBaseRepo(TestRepo testRepo){
        super.setBaseRepositories(testRepo);
    }
}
