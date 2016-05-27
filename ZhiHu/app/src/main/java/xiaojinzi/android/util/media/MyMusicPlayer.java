package xiaojinzi.android.util.media;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.media.MediaPlayer;
import xiaojinzi.android.entity.Music;
import xiaojinzi.android.util.log.L;

/**
 * Created by Administrator on 15-8-27. 应用唯一的音乐播放器
 */
public class MyMusicPlayer implements Runnable {

	public static final String TAG = "MyMusicPlayer";

	/**
	 * 暂停的状态
	 */
	public static final int PAUSE_STATE = 11;

	/**
	 * 停止状态
	 */
	public static final int STOP_STATE = 12;

	/**
	 * 准备状态
	 */
	public static final int PREPARE_STATE = 13;

	/**
	 * 正在放的状态
	 */
	public static final int PLAYING_STATE = 14;

	/**
	 * 当前播放的状态
	 */
	private int currentState = STOP_STATE;

	/**
	 * 顺序播放的模式
	 */
	public static final int ORDER_MODE = 0;

	/**
	 * 顺序循环的模式
	 */
	public static final int ORDER_CYCLE_MODE = 1;

	/**
	 * 单曲循环的模式
	 */
	public static final int SINGLE_CYCLE_MODE = 2;

	/**
	 * 随机播放的方式
	 */
	public static final int RANDOM_MODE = 3;

	/**
	 * 声明一个自己
	 */
	private static MyMusicPlayer my = null;

	/**
	 * 要播放的音乐的集合
	 */
	private List<Music> musics = null;

	/**
	 * 当前播放的下标
	 */
	private int currentIndex = -1;

	/**
	 * 当前播放的进度
	 */
	private int currentProgress = 0;

	/**
	 * 字符串形式的歌曲时长
	 */
	private String musicLength = "00:00";

	/**
	 * 歌曲的总时长
	 */
	private int musicDuration = 0;

	/**
	 * 播放的模式,默认模式为列表循环
	 */
	private static int play_mode = ORDER_CYCLE_MODE;

	/**
	 * 构造函数私有化
	 */
	private MyMusicPlayer() {
	}

	/**
	 * 获取本身的一个实例对象
	 *
	 * @return
	 */
	public static MyMusicPlayer getInstance() {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		if (my == null) {
			my = new MyMusicPlayer();
		}
		return my;
	}

	/*
	 * ===================================================================
	 * 控制音乐播放器的各个方法
	 * start====================================================================
	 * ========
	 */

	/**
	 * 监听歌曲播放完毕
	 */
	private void listenMusicComplete() {
		// 监听播放器播放结束的时候
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {

				L.s(TAG, "播放结束-------------------------88");

				currentState = STOP_STATE;
				// 循环集合通知每个现在是停止状态
				sendStateChange(STOP_STATE);
				try {
					next();
				} catch (Exception e) {
					try {
						next();
					} catch (Exception e1) {
						L.s(TAG, "播放下一首的下一首都挂了");
					}
				}
			}
		});
	}

	/**
	 * 声明一个音乐播放器
	 */
	private static MediaPlayer mediaPlayer = null;

	/**
	 * 设置要播放的音乐的下标,也就是准备状态
	 *
	 * @param index
	 */
	public MyMusicPlayer setPreparePlayItem(int index) {
		currentIndex = index;
		currentProgress = 0;
		currentState = PREPARE_STATE;

		// 通知现在是准备状态
		sendStateChange(PREPARE_STATE);
		return my;
	}

	/**
	 * 开始播放
	 * 
	 * @return
	 */
	public MyMusicPlayer start() throws Exception {
		if (musics == null || musics.size() == 0) {
			// 没有播放的歌曲就抛出异常，结束程序
			throw new Exception("没有播放的资源！请调用 setPlayData(List<Music> musics)  方法进行设置播放的资源");
		}
		// 做了健壮性的判断，下标不会越界
		if (currentIndex == -1) {
			currentIndex = 0;
		}
		if (currentIndex >= musics.size()) {
			currentIndex = musics.size() - 1;
		}

		// 监听播放完成
		listenMusicComplete();

		// 下面开始尝试播放歌曲
		mediaPlayer.reset();
		Music music = musics.get(currentIndex);
		if (music.getFd() != null) {
			mediaPlayer.setDataSource(music.getFd());
		} else {
			mediaPlayer.setDataSource(music.getPath());
		}
		mediaPlayer.prepare();
		if (currentState == PLAYING_STATE) {
			currentProgress = 0;
		}
		// 获取歌曲的总长度
		musicDuration = mediaPlayer.getDuration();

		L.s(TAG, "总长度：" + musicDuration);

		// 格式化总长度
		musicLength = formatMusciLength(musicDuration / 1000);

		mediaPlayer.seekTo(currentProgress);
		mediaPlayer.start();

		currentState = PLAYING_STATE;

		// 循环集合通知每个现在是播放状态
		sendStateChange(PLAYING_STATE);

		return this;

	}

	/**
	 * 暂停播放
	 * 
	 * @return
	 */
	public MyMusicPlayer pause() {
		currentState = PAUSE_STATE;
		currentProgress = mediaPlayer.getCurrentPosition();
		mediaPlayer.pause();
		sendStateChange(PAUSE_STATE);
		return this;
	}

	/**
	 * 停止播放
	 * 
	 * @return
	 */
	public MyMusicPlayer stop() {
		mediaPlayer.stop();
		currentProgress = 0;
		currentState = STOP_STATE;
		sendStateChange(STOP_STATE);
		return this;
	}

	/**
	 * 上一首
	 * 
	 * @return
	 */
	public MyMusicPlayer previous() throws Exception {

		// 让播放的指针归位
		currentProgress = 0;

		if (play_mode == ORDER_MODE) { // 如果是顺序播放的模式
			currentIndex--;
			if (currentIndex < 0) {
				currentIndex = 0;
				return this; // 顺序播放最前面一首的上一首是没有的，不会循环到最后一首
			}
			Integer id = musics.get(currentIndex).getId();
			if (id == null) {
				previous();
				return this;
			}
		} else if (play_mode == ORDER_CYCLE_MODE) { // 如果是顺序循环播放的模式
			currentIndex--;
			if (currentIndex < 0) {
				currentIndex = musics.size() - 1; // 顺序循环播放最后一首下一首是第一首
			}
			Integer id = musics.get(currentIndex).getId();
			if (id == null) {
				previous();
				return this;
			}
		} else if (play_mode == RANDOM_MODE) { // 如果是随机播放的模式
			Random r = new Random();
			currentIndex = r.nextInt(musics.size());
			Integer id = musics.get(currentIndex).getId();
			while (id == null) {
				currentIndex = r.nextInt(musics.size());
				id = musics.get(currentIndex).getId();
			}
		}

		return this.start();
	}

	/**
	 * 下一首
	 * 
	 * @return
	 */
	public MyMusicPlayer next() throws Exception {
		// 让播放的指针归位
		currentProgress = 0;
		if (play_mode == ORDER_MODE) { // 如果是顺序播放的模式
			currentIndex++;
			if (currentIndex >= musics.size()) {
				currentIndex = musics.size() - 1;
				return this; // 顺序播放最后一首的下一首是没有的，不会循环到第一首
			}
			Integer id = musics.get(currentIndex).getId();
			if (id == null) {
				next();
				return this;
			}
		} else if (play_mode == ORDER_CYCLE_MODE) { // 如果是顺序循环播放的模式
			currentIndex++;
			if (currentIndex >= musics.size()) {
				currentIndex = 0; // 顺序循环播放最后一首下一首是第一首
			}
			Integer id = musics.get(currentIndex).getId();
			if (id == null) {
				next();
				return this;
			}
		} else if (play_mode == RANDOM_MODE) { // 如果是随机播放的模式
			Random r = new Random();
			currentIndex = r.nextInt(musics.size());
			Integer id = musics.get(currentIndex).getId();
			while (id == null) {
				currentIndex = r.nextInt(musics.size());
				id = musics.get(currentIndex).getId();
			}
		}
		return this.start();
	}

	/*
	 * ===================================================================
	 * 控制音乐播放器的各个方法
	 * end======================================================================
	 * ======
	 */

	/**
	 * 线程执行的任务,不断的通知别人进度
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);

				// 如果集合中的人为0，则线程终止
				int size = currentProgressChange_map.size();
				if (size == 0) {
					break;
				}

				if (currentState == PLAYING_STATE) {
					// 获取当前播放的进度，并且通知集合中的每一个人
					currentProgress = mediaPlayer.getCurrentPosition();
					Set<String> set = currentProgressChange_map.keySet();
					Iterator<String> it = set.iterator();
					while (it.hasNext()) {
						String key = it.next();
						OnCurrentProgressChangeListener l = currentProgressChange_map.get(key);
						l.currentProgressChange(currentProgress);
					}
				}
			}
			L.s(TAG, "正常退出");
		} catch (Exception e) {
			L.s(TAG, "线程异常");
		}
	}

	/*
	 * ===================================================================
	 * 提供的各种注册监听
	 * start====================================================================
	 * ========
	 */

	/**
	 * 状态通知要用到的过滤集合 发送通知的时候,要被过滤掉的,就是这个map决定了谁能接受到消息
	 */
	private Map<String, Boolean> stateListenFilterMap = new HashMap<String, Boolean>();

	/**
	 * 设置某一个key对应的{@link #stateChange_map}的接口对象是否能就受到消息,true标识会被屏蔽,反之则能收到消息
	 *
	 * @param key
	 * @param filter
	 *            true标识会被屏蔽,反之则能收到消息
	 */
	public void setStateFilter(String key, boolean filter) {
		stateListenFilterMap.put(key, filter);
	}

	/**
	 * 状态改变接口对象的集合
	 */
	private Map<String, OnStateChangeListener> stateChange_map = new HashMap<String, OnStateChangeListener>();

	/**
	 * 设置监听播放器状态的接口
	 *
	 * @param stateChange
	 */
	public void addOnStateChange(String key, OnStateChangeListener stateChange) {
		stateChange_map.put(key, stateChange);
		stateListenFilterMap.put(key, false); // 标识不被过滤,可以接受到消息
	}

	/**
	 * 移除监听播放器的状态
	 *
	 * @param key
	 */
	public void removeOnStateChange(String key) {
		stateChange_map.remove(key);
		stateListenFilterMap.remove(key);
	}

	/**
	 * 当前播放进度发生改变的接口的集合,改良版，添加监听需要使用对应的字符串的key
	 */
	private Map<String, OnCurrentProgressChangeListener> currentProgressChange_map = new HashMap<String, OnCurrentProgressChangeListener>();

	/**
	 * 添加播放进度发生改变的接口,特别注意的是, 这个内部使用子线程实现不断获取进度,所以
	 * 通知的方法也是子线程调用的,监听了不可直接修改主线程的ui界面
	 *
	 * @param timeChangeListener
	 */
	public void addOnCurrentProgressChangeListener(String key, OnCurrentProgressChangeListener timeChangeListener) {
		currentProgressChange_map.put(key, timeChangeListener);
		if (currentProgressChange_map.size() == 1) {
			new Thread(this).start();
		}
	}

	/**
	 * 移除一个播放进度监听
	 *
	 * @param key
	 */
	public void removeOnCurrentProgressChangeListener(String key) {
		currentProgressChange_map.remove(key);
	}

	/**
	 * 所有监听播放模式的接口的集合
	 */
	private Map<String, OnPlayModeChangeListener> onPlayModeChangeListeners = new HashMap<String, OnPlayModeChangeListener>();

	/**
	 * 添加一个监听播放模式的接口
	 *
	 * @param key
	 * @param onPlayModeChangeListener
	 */
	public void addOnPlayModeChangeListener(String key, OnPlayModeChangeListener onPlayModeChangeListener) {
		onPlayModeChangeListeners.put(key, onPlayModeChangeListener);
	}

	/**
	 * 移除一个监听播放模式的接口
	 *
	 * @param key
	 */
	public void removeOnPlayModeChangeListener(String key) {
		onPlayModeChangeListeners.remove(key);
	}

	/**
	 * 所有监听播放模式的接口的集合
	 */
	private Map<String, OnDataChangeListener> onDataChangeListeners = new HashMap<String, OnDataChangeListener>();

	/**
	 * 添加一个监听使用数据的改变的接口
	 *
	 * @param key
	 * @param onDataChangeListener
	 */
	public void addOnDataChangeListener(String key, OnDataChangeListener onDataChangeListener) {
		onDataChangeListeners.put(key, onDataChangeListener);
	}

	/**
	 * 移除一个监听使用数据的改变的接口
	 *
	 * @param key
	 */
	public void removeOnDataChangeListener(String key) {
		onDataChangeListeners.remove(key);
	}

	/**
	 * 移除所有的集合中的监听,某一个标识对应的所有
	 *
	 * @param key
	 */
	public void removeAll(String key) {
		this.removeOnCurrentProgressChangeListener(key);
		this.removeOnStateChange(key);
		this.removeOnPlayModeChangeListener(key);
		this.removeOnDataChangeListener(key);
	}

	/*
	 * ===================================================================
	 * 提供的各种注册监听
	 * end======================================================================
	 * ======
	 */

	/*
	 * ===================================================================
	 * 各种get和set方法
	 * start====================================================================
	 * ========
	 */

	/**
	 * 返回音乐播放器对象
	 * 
	 * @return
	 */
	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	/**
	 * 获取播放的方式
	 *
	 * @return
	 */
	public int getPlay_mode() {
		return play_mode;
	}

	/**
	 * 获取当前播放的下标
	 *
	 * @return
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * 获取当前播放的状态
	 *
	 * @return
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * 获取字符串的歌曲的时长
	 *
	 * @return
	 */
	public String getMusicLength() {
		return musicLength;
	}

	/**
	 * 获取整形的歌曲总时长
	 *
	 * @return
	 */
	public int getMusicDuration() {
		return musicDuration;
	}

	/**
	 * 获取当前的播放音乐
	 *
	 * @return
	 */
	public Music getCurrentMusic() {
		try {
			return this.musics.get(currentIndex);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 返回是否在播放
	 *
	 * @return
	 */
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	/**
	 * 设置的进度
	 *
	 * @param progress
	 * @return
	 */
	public MyMusicPlayer setProgress(int progress) {
		if (currentState == PLAYING_STATE || currentState == PAUSE_STATE) {
			if (progress >= musicDuration) {
				progress = musicDuration - 1000;
			}
			currentProgress = progress;
			mediaPlayer.seekTo(progress);
		}
		return this;
	}

	/**
	 * 设置播放方式
	 * 
	 * @param mode
	 * @return
	 */
	public MyMusicPlayer setPlayMode(int mode) {
		play_mode = mode;
		// 播放模式改变之后通知别人
		sendPlayModeChange(play_mode);
		return this;
	}

	/**
	 * 设置播放的资源,这里是一个集合对象
	 *
	 * @param musics
	 */
	public MyMusicPlayer setPlayData(List<Music> musics) {
		this.musics = musics;
		// 发送使用的数据发生改变的通知
		sendDataChange(musics);
		return this;
	}

	/**
	 * 返回所有歌曲的集合
	 *
	 * @return
	 */
	public List<Music> getPlayData() {
		return musics;
	}

	/*
	 * ===================================================================
	 * 各种get和set方法
	 * end======================================================================
	 * ======
	 */

	/**
	 * 退出的方法,释放资源
	 */
	public void exit() {
		// 通知别人我停止了
		currentState = STOP_STATE;
		this.sendStateChange(STOP_STATE);

		currentProgress = 0;

		// 清空监听进度的集合
		currentProgressChange_map.clear();

		musics = null;

		// 释放播放器的资源
		mediaPlayer.reset();
		mediaPlayer.release();
		mediaPlayer = null;
	}

	/**
	 * 格式化歌曲的时长 400 --> 06:40
	 *
	 * @param duration
	 * @return
	 */
	public static String formatMusciLength(int duration) {
		// 获取秒
		int seconds = duration % 60;
		// 获取分钟
		int minute = (duration - seconds) / 60;

		String tmp = "";

		if (("" + minute).length() == 1) {
			tmp += "0" + minute + ":";
		} else {
			tmp += minute + ":";
		}

		if (("" + seconds).length() == 1) {
			tmp += "0" + seconds;
		} else {
			tmp += seconds;
		}
		return tmp;
	}

	/**
	 * 发送给监听的用户现在的播放状态
	 *
	 * @param playingState
	 */
	private void sendStateChange(int playingState) {
		Set<String> set = stateChange_map.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Boolean b = stateListenFilterMap.get(key); // 获取是否屏蔽这个ket对应的接口
			if (!b) { // 如果没有屏蔽就发送消息
				OnStateChangeListener l = stateChange_map.get(key);
				l.stateChange(playingState);
			}
		}
	}

	/**
	 * 发送给监听的用户播放模式改变
	 *
	 * @param playMode
	 */
	private void sendPlayModeChange(int playMode) {
		Set<String> set = onPlayModeChangeListeners.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			OnPlayModeChangeListener l = onPlayModeChangeListeners.get(key);
			l.playModeChange(playMode);
		}
	}

	/**
	 * 当使用的数据发生改变的时候调用
	 *
	 * @param list
	 */
	private void sendDataChange(List<Music> list) {
		Set<String> set = onDataChangeListeners.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			OnDataChangeListener l = onDataChangeListeners.get(key);
			l.dataChange(list);
		}
	}

	/**
	 * 重新发送当前的播放状态
	 */
	public void reSendStateChange() {
		sendStateChange(this.currentState);
	}

	/*
	 * ===================================================================
	 * 定义的各种接口
	 * start====================================================================
	 * ========
	 */

	/**
	 * 当集合发生改变的时候,包括第一次的初始化集合
	 */
	public interface OnDataChangeListener {
		/**
		 * 当使用的集合发送改变的时候触发,包括第一次初始化集合
		 *
		 * @param list
		 */
		public void dataChange(List<Music> list);
	}

	/**
	 * 播放模式的改变的接口定义
	 */
	public interface OnPlayModeChangeListener {
		/**
		 * 当播放器的播放模式发生改变的时候触发
		 *
		 * @param playMode
		 */
		public void playModeChange(int playMode);
	}

	/**
	 * 设置播放状态发生改变的接口
	 */
	public interface OnStateChangeListener {
		/**
		 * 当状态发生改变的时候触发这个事件
		 */
		public void stateChange(int state);
	}

	/**
	 * 当前播放进度发生改变的接口
	 */
	public interface OnCurrentProgressChangeListener {
		/**
		 * 当前播放状态发生改变触发这个事件
		 *
		 * @param currentProgress
		 */
		public void currentProgressChange(int currentProgress);
	}

	/*
	 * ===================================================================
	 * 定义的各种接口
	 * end======================================================================
	 * ======
	 */

}
