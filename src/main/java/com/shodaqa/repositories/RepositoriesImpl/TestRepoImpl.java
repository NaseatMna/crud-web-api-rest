package com.shodaqa.repositories.RepositoriesImpl;

import com.shodaqa.models.Test;
import com.shodaqa.repositories.TestRepo;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by Naseat_PC on 9/12/2017.
 */
@Repository
@Transactional
public class TestRepoImpl extends BaseRepositoriesImpl<Test, Long> implements TestRepo {
}
