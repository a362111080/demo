package com.zero.egg.service;

import com.zero.egg.model.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.egg.requestDTO.TaskRequest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Hhaifeng
 * @since 2019-03-25
 */
public interface ITaskService extends IService<Task> {

    List<Task> QueryTaskList(TaskRequest task);
}
