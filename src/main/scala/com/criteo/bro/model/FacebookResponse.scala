package com.criteo.bro.model

case class Recipient(id: String)


case class FacebookPayload(template_type: String, elements: List[FacebookGenericElement])
case class FacebookAttachment(atttype: String, payload: FacebookPayload)
case class FacebookMessage(attachment: FacebookAttachment)
case class FacebookResponse(recipient: Recipient, message: FacebookMessage)

case class FacebookButton(btntype: String, title: String, url: String)
case class DefaultAction(actiontype: String, url: String)
case class FacebookGenericElement(title: String, subtitle: String, image_url: String, default_action: DefaultAction, buttons: List[FacebookButton])