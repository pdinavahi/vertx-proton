package io.vertx.proton;

import org.apache.qpid.proton.amqp.transport.DeliveryState;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public interface ProtonDelivery {

  /**
   * Updates the DeliveryState, and optionally settle the delivery as well.
   *
   * @param state
   *          the delivery state to apply
   * @param settle
   *          whether to {@link #settle()} the delivery at the same time
   * @return itself
   */
  ProtonDelivery disposition(DeliveryState state, boolean settle);

  DeliveryState getLocalState();

  DeliveryState getRemoteState();

  ProtonDelivery settle();

  boolean remotelySettled();

  byte[] getTag();

  int getMessageFormat();
}
