package org.myrobotlab.service;

import java.util.List;

import org.myrobotlab.framework.Service;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.logging.Logging;
import org.myrobotlab.logging.LoggingFactory;
import org.myrobotlab.service.data.PinData;
import org.myrobotlab.service.interfaces.PinArrayControl;
import org.myrobotlab.service.interfaces.PinListener;
import org.slf4j.Logger;

public class Pir extends Service implements PinListener {

  public final static Logger log = LoggerFactory.getLogger(Pir.class);

  private static final long serialVersionUID = 1L;

  public static void main(String[] args) {
    try {

      LoggingFactory.init("info");

      Pir pir = (Pir) Runtime.start("pir", "Pir");
      Runtime.start("gui", "SwingGui");
      String arduinoPort = "COM4";

      VirtualArduino virtual = (VirtualArduino) Runtime.start("virtual", "VirtualArduino");
      Arduino arduino = (Arduino) Runtime.start("arduino", "Arduino");
      virtual.connect(arduinoPort);
      arduino.connect(arduinoPort);
      arduino.setBoardMega();
      pir.attach(arduino, 2);
      pir.enable();

    } catch (Exception e) {
      Logging.logError(e);
    }
  }

  boolean isActive = false;
  boolean isEnabled = false;
  public boolean isVerbose = true;

  Integer pin;

  PinArrayControl pinControl;
  List<String> controllers;

  public Pir(String n, String id) {
    super(n, id);
  }

  public void attach(PinArrayControl control, int pin) {
    this.pinControl = control;
    this.pin = pin;
    pinControl.attach(this, pin);
  }

  public void disable() {
    if (pinControl == null) {
      error("pin control not set");
      return;
    }

    if (pin == null) {
      error("pin not set");
      return;
    }

    pinControl.disablePin(pin);
    isEnabled = false;
    broadcastState();
  }

  public void enable() {
    enable(1);
  }

  public void enable(int pollBySecond) {
    if (pinControl == null) {
      error("pin control not set");
      return;
    }

    if (pin == null) {
      error("pin not set");
      return;
    }

    pinControl.enablePin(pin, pollBySecond);
    isEnabled = true;
    broadcastState();
  }

  public List<String> refresh() {
    controllers = Runtime.getServiceNamesFromInterface(PinArrayControl.class);
    broadcastState();
    return controllers;
  }

  @Override
  public void onPin(PinData pindata) {
    if (isVerbose) {
      log.info("onPin {}", pindata);
    }
    boolean sense = (pindata.value != 0);

    if (isActive != sense) {
      // state change
      invoke("publishSense", sense);
      isActive = sense;
    }
  }

  public Boolean publishSense(Boolean b) {
    return b;
  }

  public void setPin(int pin) {
    this.pin = pin;
    broadcastState();
  }

  public void setPinArrayControl(PinArrayControl pinControl) {
    this.pinControl = pinControl;
    broadcastState();
  }

}
