package com.oi.hermes.subscription.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


@Entity
public class Subscription {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String plan_free_quantity;
	private String remaining_billing_cycles;
	@NotNull
	private String plan_quantity;
	@NotNull
	private String plan_id;
	
	@Column(name = "subscriptionId", length = 50, unique = true)
	@NotNull(message = "subscriptionId can not be null.")
	private String subscriptionId;

	private String has_scheduled_changes;

	private String resource_version;


	private String trial_end;

	private String current_term_start;


	private String total_dues;

	private String plan_unit_price;

	private String current_term_end;

	private String billing_period_unit;

	private String created_at;

	private String started_at;

	private String activated_at;

	private String currency_code;

	private String status;

	private String cancelled_at;

	private String deleted;

	private String due_invoices_count;

	private String billing_period;

	private String due_since;

	private String updated_at;

	private String trial_start;

	public String getPlan_free_quantity() {
		return plan_free_quantity;
	}

	public void setPlan_free_quantity(String plan_free_quantity) {
		this.plan_free_quantity = plan_free_quantity;
	}

	public String getRemaining_billing_cycles() {
		return remaining_billing_cycles;
	}

	public void setRemaining_billing_cycles(String remaining_billing_cycles) {
		this.remaining_billing_cycles = remaining_billing_cycles;
	}

	public String getPlan_quantity() {
		return plan_quantity;
	}

	public void setPlan_quantity(String plan_quantity) {
		this.plan_quantity = plan_quantity;
	}

	public String getHas_scheduled_changes() {
		return has_scheduled_changes;
	}

	public void setHas_scheduled_changes(String has_scheduled_changes) {
		this.has_scheduled_changes = has_scheduled_changes;
	}

	public String getResource_version() {
		return resource_version;
	}

	public void setResource_version(String resource_version) {
		this.resource_version = resource_version;
	}


	public String getTrial_end() {
		return trial_end;
	}

	public void setTrial_end(String trial_end) {
		this.trial_end = trial_end;
	}

	public String getCurrent_term_start() {
		return current_term_start;
	}

	public void setCurrent_term_start(String current_term_start) {
		this.current_term_start = current_term_start;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getTotal_dues() {
		return total_dues;
	}

	public void setTotal_dues(String total_dues) {
		this.total_dues = total_dues;
	}

	public String getPlan_unit_price() {
		return plan_unit_price;
	}

	public void setPlan_unit_price(String plan_unit_price) {
		this.plan_unit_price = plan_unit_price;
	}

	public String getCurrent_term_end() {
		return current_term_end;
	}

	public void setCurrent_term_end(String current_term_end) {
		this.current_term_end = current_term_end;
	}

	public String getBilling_period_unit() {
		return billing_period_unit;
	}

	public void setBilling_period_unit(String billing_period_unit) {
		this.billing_period_unit = billing_period_unit;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getStarted_at() {
		return started_at;
	}

	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}

	public String getActivated_at() {
		return activated_at;
	}

	public void setActivated_at(String activated_at) {
		this.activated_at = activated_at;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCancelled_at() {
		return cancelled_at;
	}

	public void setCancelled_at(String cancelled_at) {
		this.cancelled_at = cancelled_at;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDue_invoices_count() {
		return due_invoices_count;
	}

	public void setDue_invoices_count(String due_invoices_count) {
		this.due_invoices_count = due_invoices_count;
	}

	public String getBilling_period() {
		return billing_period;
	}

	public void setBilling_period(String billing_period) {
		this.billing_period = billing_period;
	}

	public String getDue_since() {
		return due_since;
	}

	public void setDue_since(String due_since) {
		this.due_since = due_since;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getTrial_start() {
		return trial_start;
	}

	public void setTrial_start(String trial_start) {
		this.trial_start = trial_start;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
	
}