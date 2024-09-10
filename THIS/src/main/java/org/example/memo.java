
//                    }else if(uo ==3)
//
//        {
//
//        while(true){
//        System.out.
//
//println();
//
//            System.out.
//
//println("예금주 조회");
//            System.out.
//
//print("조회할 계좌번호: ");
//
//int aNumber = sc.nextInt();
//
//            try{
//pstmt =conn.
//
//prepareStatement("SELECT * FROM owners WHERE a_number = ?");
//                pstmt.
//
//setInt(1,aNumber);
//
//rs =pstmt.
//
//executeQuery();
//
//                if(rs.
//
//next()){    //존재하는 계좌라면
//
//        try{
//        System.out.
//
//println();
//                        System.out.
//
//println("예금주 목록: ");
//
//pstmt =conn.
//
//prepareStatement("select * from owners where a_number = ? order by u_idx desc");
//                        pstmt.
//
//setInt(1,aNumber);
//
//ResultSet rs = pstmt.executeQuery();
//
//                        while(rs.
//
//next()){
//Owners owners = Owners.builder()
//        .u_idx(rs.getInt("u_idx"))
//        .a_number(rs.getInt("a_number"))
//        .build();
//                            list.
//
//add(owners);
//                        }
//                                list.
//
//stream()
//                                .
//
//forEach(System.out::println);
//                    }catch(
//Exception e){
//        e.
//
//printStackTrace();
//                    }
//                            break;
//                            }else{
//                            System.out.
//
//println("존재하지 않는 계좌입니다.");
//                    System.out.
//
//println();
//                }
//
//
//                        }catch(
//Exception e){
//        e.
//
//printStackTrace();
//
//            }
//                    break;
//                    }
//
//
//                    }else
//
//                    {
//                    new
//
//ClerkMenu6();
//        break;
//
//                }
////            System.out.println("올바른 번호를 입력하세요.");
//
//
//                }
//
//
//                }
