package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.AuctionProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AddressSemanticsRepository extends JpaRepository<AddressSemantics, Long>
{
    /**
     * Search for an address index by first line and last line. Be aware that
     * lastLine is of the format "[CITY] [STATE] [ZIP]" with no commas.
     */
    AddressSemantics findTopByFirstLineAndLastLine(String firstLine, String lastLine, Sort sort);

    @Query("SELECT a FROM AddressSemantics a WHERE a.firstLine LIKE :address%")
    Page<AddressSemantics> searchByFirstLine(@Param("address") String address, Pageable pageable);

    AddressSemantics findTopByNtreisProperty(NtreisProperty ntreisProperty);

    AddressSemantics findByAuctionProperty(AuctionProperty auctionProperty);

    AddressSemantics findTopByTrestleSystemAndTrestleId(String trestleSystem, Long trestleId);

    @Query(value = "SELECT * FROM address_semantics WHERE auction_id = ?1 LIMIT 1", nativeQuery = true)
    AddressSemantics findByAuctionId(int auctionId);

    @Query(value = "SELECT * FROM address_semantics WHERE auction_id in ?1", nativeQuery = true)
    List<AddressSemantics> findAllByAuctionId(List<Integer> auctionIds);

    Page<AddressSemantics> findAllByTaxIdIsNotNull(Pageable page);
}
