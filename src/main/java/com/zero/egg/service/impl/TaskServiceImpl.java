package com.zero.egg.service.impl;

import com.zero.egg.model.Task;
import com.zero.egg.dao.TaskMapper;
import com.zero.egg.requestDTO.TaskRequest;
import com.zero.egg.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private TaskMapper mapper;

    @Override
    public List<Task> QueryTaskList(TaskRequest task) {
        return mapper.QueryTaskList(task);
    }
}
