/**
 * @author Utkarsh Srivastava
 */
import { MockEmailService } from '../services/emailService';

describe('MockEmailService', () => {
  it('should successfully record and send enquiry email', async () => {
    const service = new MockEmailService('sales@shopora.com');
    const result = await service.sendEnquiryEmail({
      productId: 1,
      productTitle: 'Fjallraven - Foldsack No. 1 Backpack',
      customerName: 'Alice Smith',
      customerEmail: 'alice@example.com',
      message: 'Is this backpack waterproof?',
    });

    expect(result.success).toBe(true);
    expect(result.enquiryId).toContain('ENQ-');
    expect(service.getSentEmails()).toHaveLength(1);
    expect(service.getSentEmails()[0].customerEmail).toBe('alice@example.com');
  });
});
