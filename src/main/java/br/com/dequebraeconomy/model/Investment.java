package br.com.dequebraeconomy.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Investment extends Transaction {
    private LocalDate payoutDate;
    private String investmentName;
    private String issuingBank;
    private boolean taxable;
    private BigDecimal interestRate;

    public Investment(long id, long userId, LocalDate date, BigDecimal amount, String description, LocalDate payoutDate, String investmentName, String issuingBank, boolean taxable, BigDecimal interestRate,Category category){
        super(id, date, amount, description, category, userId);
        this.payoutDate = payoutDate;
        this.investmentName = investmentName;
        this.issuingBank  = issuingBank;
        this.taxable = taxable;
        this.interestRate = interestRate;
    }


    public LocalDate getPayoutDate() {
        return payoutDate;
    }

    public void setPayoutDate(LocalDate payoutDate) {
        this.payoutDate = payoutDate;
    }

    public String getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(String investmentName) {
        this.investmentName = investmentName;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }


    public static LocalDate today(){
        return  LocalDate.now();
    }


    public long daysInApplication(){
        return  ChronoUnit.DAYS.between(getDate(),this.payoutDate);
    }


    /**
     *Calcula o rendimento bruto do investimento com base:
     *  - No valor aplicado (considerado em valor absoluto)
     *  - Na taxa de juros atual
     *  - No tempo proporcional em anos (dias / 365)
     *
     *  Formula: valor ivestido  * taxa * (dias / 365)
     * @return rendimento bruto com escala de 2 casas decimais
     *
     */

    public BigDecimal calculateGrossReturn(){
        BigDecimal investedAmount = getAmount().abs();

        BigDecimal rate = getInterestRate()
                .divide(new BigDecimal("100"), 10 , RoundingMode.HALF_UP);

        BigDecimal days = BigDecimal.valueOf(daysInApplication());

        BigDecimal proportionalAYear = days
                .divide(BigDecimal.valueOf(365), 10 , RoundingMode.HALF_UP);

        BigDecimal grossReturn = investedAmount
                .multiply(rate).multiply(proportionalAYear);


    return grossReturn.setScale(2,RoundingMode.HALF_UP);
    }


    /**
     * Calcula o imposto de renda sobre o rendimento bruto.
     *
     * A líquota varia conforme o tempo aplicado:
     *
     * - Até 180 dias: 22.5%
     * - Até 360 dias: 20.0%
     * - Até 720 dias: 17.5%
     * - Acima de 720: 15%
     *
     * Caso o investimento não seja tributável,
     * retorna BigDecimal.ZERO.
     *
     * @retur valor do imposto de renda.
     *
     */

    public BigDecimal calculateIncomeTax(){

        if(!taxable){
            return BigDecimal.ZERO;
        }
        BigDecimal grossReturn = calculateGrossReturn();
        long days = daysInApplication();
        BigDecimal taxRate;
        if(days <= 180){
            taxRate = new BigDecimal("22.5");
        }else if(days <= 360){
            taxRate =  new BigDecimal("20.0");
        }else if(days <= 720){
            taxRate = new BigDecimal("17.5");
        }else{
            taxRate = new BigDecimal("15.0");
        }

        taxRate = taxRate
                .divide(new BigDecimal("100"), 10 , RoundingMode.HALF_UP);

        BigDecimal incomeTax = grossReturn
                .multiply(taxRate);

        return incomeTax.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     *
     * - O rendimento liquido corresponde ao rendimento bruto menos o imposto de renda, quando aplicavel.
     *  - Subtrai o rendimento ao valor

     *  */


    public BigDecimal calculateNetReturn(){

        return calculateGrossReturn()
                .subtract(calculateIncomeTax());
    }

    /**
     * Calcula o valor a ser recebido no vencimento do investimento
     * - O total é composto  pelo valor originalmente aplicado (considerdo em valor absoluto, pois é armazenado como negativo)
     * somado ao rendimento liquido.
     * - O rendimento liquido corresponde ao rendimento bruto menos o imposto de renda, quando aplicavel.
     *
     * @return valor total disponível no vencimento.
     */
    public BigDecimal calculateTotalAtMaturity(){
        return getAmount().abs().add(calculateNetReturn());
    }


    @Override
    public String showTransaction() {
        return String.format("""
                %s: 
                Nome: %s
                Descrição: %s
                Valor:  %s
                Data: %s
                Data de venciento: %s
                Instituição Financeira %s
                Sujeito a IR: %b
                Taxa de rendimento: %.2f
                Rendimento Bruto: %s
                Rendimento liquido: %s
                Total no vencimento: %s 
                """,getCategory(),getInvestmentName(),getDescription(), getAmount().abs().setScale(2, RoundingMode.HALF_UP), getDate(),getPayoutDate(),getIssuingBank(),isTaxable(),getInterestRate(), calculateGrossReturn().setScale(2,RoundingMode.HALF_UP),calculateNetReturn().setScale(2,RoundingMode.HALF_UP),calculateTotalAtMaturity().setScale(2,RoundingMode.HALF_UP));
    }
}
