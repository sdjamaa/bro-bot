package com.criteo.bro.model

case class Recipient(id: String)


case class FacebookPayload(template_type: String, elements: List[FacebookGenericElement])
case class FacebookAttachment(atttype: String, payload: FacebookPayload)
case class FacebookMessage(attachment: FacebookAttachment)
case class FacebookResponse(recipient: Recipient, message: FacebookMessage)

case class RequestedUserInfo(shipping_address: String, contact_name: String, contact_phone: String, contact_email: String)
case class Price(label: String, amount: String)
case class PaymentSummary(currency: String, payment_type: String, is_test_payment: Boolean, merchant_name: String, requested_user_info: List[RequestedUserInfo], price_list: List[Price])
case class FacebookButton(btntype: String, title: String, payload: String, payment_summary: PaymentSummary)
case class DefaultAction(actiontype: String,title: String, url: String)
case class FacebookGenericElement(title: String, subtitle: String, image_url: String, default_action: DefaultAction, buttons: List[FacebookButton])