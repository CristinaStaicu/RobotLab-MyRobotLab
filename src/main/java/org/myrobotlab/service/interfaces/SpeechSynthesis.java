package org.myrobotlab.service.interfaces;

import java.util.List;

import org.myrobotlab.framework.interfaces.NameProvider;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.service.abstracts.AbstractSpeechSynthesis.Voice;
import org.myrobotlab.service.data.AudioData;
import org.slf4j.Logger;

/**
 * SpeechSynthesis - This is the interface that services that provide text to
 * speech should implement.
 * 
 */
public interface SpeechSynthesis extends NameProvider, TextListener, LocaleProvider {

  public final static Logger log = LoggerFactory.getLogger(SpeechSynthesis.class);

  public String getlastUtterance();

  /**
   * set the speaker voice
   * 
   * @param voice
   * @return
   */
  public boolean setVoice(String voice);

  /**
   * Begin speaking something and return immediately
   * 
   * @param toSpeak
   *          - the string of text to speak.
   * @return TODO
   * @throws Exception
   *           e
   */
  public List<AudioData> speak(String toSpeak) throws Exception;

  /**
   * Begin speaking and wait until all speech has been played back/
   * 
   * @param toSpeak
   *          - the string of text to speak.
   * @throws Exception
   *           e
   * @return true/false
   */
  public List<AudioData> speakBlocking(String toSpeak) throws Exception;

  /**
   * Change audioData volume
   * 
   * @param volume
   *          - double between 0 and 1.
   */
  public void setVolume(double volume);

  /**
   * Get audioData volume
   * 
   * @return double
   */
  public double getVolume();

  /**
   * Get current voice
   * 
   * @return Voice
   */
  public Voice getVoice();

  /**
   * start callback for speech synth. (Invoked when speaking starts)
   * 
   * @param utterance
   *          text
   * @return the same text
   */
  public String publishStartSpeaking(String utterance);

  /**
   * stop callback for speech synth. (Invoked when speaking stops.)
   * 
   * @param utterance
   *          text
   * @return text
   */
  public String publishEndSpeaking(String utterance);

  /**
   * silence the service
   */
  @Deprecated /* use setMute */
  public void mute();

  /**
   * un-silence the service
   */
  @Deprecated /* use setMute */
  public void unmute();
  
  /**
   * mute or unmute 
   * @param mute
   */
  public void setMute(boolean mute);

  @Deprecated /* this should be type specific named - use attachSpeechRecognizer*/
  public void addEar(SpeechRecognizer ear);

  // FIXME - is this in the wrong place ??? - this seems like bot logic ...
  public void onRequestConfirmation(String text);

  /**
   * get a list of voices this speech synthesis supports
   * @return
   */
  public List<Voice> getVoices();
  
  /**
   * puts all speaking into blocking mode - default is false
   * @param b
   * @return
   */
  public Boolean setBlocking(Boolean b);

  /**
   * This attach subscribes the the SpeechRecognizer to the SpeechSynthesizer so the bot won't incorrectly
   * recognize itself when its speaking ... otherwise silly things can happen when talking to self...
   * 
   * @param ear
   */
  public void attachSpeechRecognizer(SpeechRecognizer ear);
  
  /**
   * event end-point for SpeechSynthesizerControl.publishSetVoice
   * 
   * @param name new name of voice
   */
  public void onSetVoice(String name);
  
  /**
   * event end-point for SpeechSynthesizerControl.publishSetVolume
   * @param volume - new volume
   */
  public void onSetVolume(Double volume);
  
  /**
   * event end-point for SpeechSynthesizerControl.speak
   * @param text
   */
  public void onSpeak(String text);
  
  /**
   * Speech control controls volume, setting the voice, and of course "speak"
   * @param control
   */
  public void attachSpeechControl(SpeechSynthesisControl control);

}