package cn.com.easy.utils;

import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Quartz scheduler manager
 * 
 * @author chlingm
 * @date 2015-03-09
 * @version V1.0
 */
public class QuartzUtils {

	private static Logger LOG = LoggerFactory.getLogger(QuartzUtils.class);

	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

	private static String JOB_GROUP_NAME = "DEFAULT_JOBGROUP_NAME";

	private static String TRIGGER_GROUP_NAME = "DEFAULT_TRIGGERGROUP_NAME";

	public static void main(String[] args) {
		QuartzUtils.addJob("my", new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					System.out.println("run my job!");
				}

			}
		}, "00 58 17 * * ?");
	}

	/**
	 * 
	 * @param jobName
	 * @param runnable
	 * @param cronExpretion
	 * @auth zhanglj 2015-3-30
	 */
	public static void addJob(String jobName, Runnable runnable, String cronExpretion) {

		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, MyJobProxy.class);
			jobDetail.getJobDataMap().put("myjob", runnable);
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);
			trigger.setCronExpression(cronExpretion);
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static class MyJobProxy implements Job {

		private Runnable job = null;

		public void execute(JobExecutionContext context) throws JobExecutionException {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			this.job = (Runnable) jobDataMap.get("myjob");
			this.job.run();
		}
	}

	/**
	 * add a job
	 * 
	 * @param jobName
	 *            job name
	 * @param cls
	 *            job class
	 * @param time
	 *            crond expression
	 */
	public static void addJob(String jobName, Class<?> cls, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);
			trigger.setCronExpression(time);
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * add a job
	 * 
	 * @param jobName
	 *            job name
	 * @param jobGroupName
	 *            job group name
	 * @param triggerName
	 *            trigger name
	 * @param triggerGroupName
	 *            trigger group name
	 * @param jobClass
	 *            job class
	 * @param time
	 *            crond expression
	 * @param params
	 *            to set on context
	 */

	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<?> jobClass, String time, Map<String, Object> params) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);
			trigger.setCronExpression(time);
			// set params to context
			if (params != null) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					sched.getContext().put(entry.getKey(), entry.getValue());
				}
			}
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
			LOG.debug("###Add Job Succeed!!jobName = [{}],jobGroupName = [{}],triggerName = [{}]," + "triggerGroupName = [{}],cronExp = [{}] ###", jobName, jobGroupName, triggerName,
					triggerGroupName, time);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * modify job
	 * 
	 * @param jobName
	 * @param time
	 */
	public static void modifyJobTime(String jobName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
				Class<?> objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * modify job time
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 */
	public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
			if (trigger == null) {
				return;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				CronTrigger ct = (CronTrigger) trigger;
				// 修改时间
				ct.setCronExpression(time);
				// 重启触发器
				sched.resumeTrigger(triggerName, triggerGroupName);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * remove a job
	 * 
	 * @param jobName
	 */
	public static void removeJob(String jobName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
			sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * remove a job
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerName, triggerGroupName);// stop trigger
			sched.unscheduleJob(triggerName, triggerGroupName);// remove trigger
			sched.deleteJob(jobName, jobGroupName);// delete job
			LOG.debug("###Remove Job Succeed!!jobName = [{}],jobGroupName = [{}],triggerName = [{}]," + "triggerGroupName = [{}],cronExp = [{}] ###", jobName, jobGroupName, triggerName,
					triggerGroupName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * start all jobs
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * shutdown all jobs
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}