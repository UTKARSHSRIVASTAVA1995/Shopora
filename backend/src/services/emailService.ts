/**
 * @author Utkarsh Srivastava
 */
import { EnquiryRequest, EnquiryResponse } from '../models/types';

export interface EmailService {
  sendEnquiryEmail(enquiry: EnquiryRequest): Promise<EnquiryResponse>;
}

export class MockEmailService implements EmailService {
  private salesInbox: string;
  private sentEmails: Array<EnquiryRequest & { sentAt: string; enquiryId: string }> = [];

  constructor(salesInbox: string = process.env.SALES_INBOX_EMAIL || 'sales@shopora.com') {
    this.salesInbox = salesInbox;
  }

  async sendEnquiryEmail(enquiry: EnquiryRequest): Promise<EnquiryResponse> {
    const enquiryId = `ENQ-${Date.now()}-${Math.floor(Math.random() * 1000)}`;
    const timestamp = new Date().toISOString();

    console.log(`\n==================================================`);
    console.log(`[EMAIL SERVICE] Sending Sales Enquiry to: ${this.salesInbox}`);
    console.log(`Enquiry ID: ${enquiryId}`);
    console.log(`Product ID: ${enquiry.productId} - "${enquiry.productTitle}"`);
    console.log(`From Customer: ${enquiry.customerName} <${enquiry.customerEmail}>`);
    console.log(`Message: "${enquiry.message}"`);
    console.log(`Sent At: ${timestamp}`);
    console.log(`==================================================\n`);

    this.sentEmails.push({
      ...enquiry,
      sentAt: timestamp,
      enquiryId,
    });

    return {
      success: true,
      message: 'Thank you! Your enquiry has been received by our sales team.',
      enquiryId,
      timestamp,
    };
  }

  getSentEmails() {
    return this.sentEmails;
  }
}
